/* empty css                */
import { _ as _export_sfc, s as document_copy_default, c as createElementBlock, b as createBaseVNode, v as withDirectives, a as createVNode, w as withCtx, r as resolveComponent, x as resolveDirective, q as normalizeStyle, e as axios, f as api, E as ElMessage, i as ElMessageBox, o as openBlock, g as createTextVNode } from "./index-BHtkpZle.js";
const _sfc_main = {
  name: "Admoviecontroller",
  data() {
    return {
      search: "",
      // 搜索关键字
      tableData: [],
      moviesData: [],
      directorsData: [],
      actorsData: [],
      movieTypes: [],
      editDialogVisible: false,
      // 控制编辑弹窗显示
      importLoading: false,
      addDialogVisible: false,
      // 控制添加弹窗显示
      addForm: {
        title: "",
        score: "",
        description: "",
        movieTypeIds: [],
        isVip: "",
        directorIds: [],
        actors: []
      },
      editForm: {
        id: "",
        title: "",
        score: "",
        movieTypes: [],
        description: "",
        isVip: "",
        directors: [],
        actors: []
      }
    };
  },
  components: {
    DocumentCopy: document_copy_default
  },
  computed: {
    filterTableData() {
      if (!this.search) return this.tableData;
      return this.tableData.filter(
        (item) => item.title.includes(this.search) || String(item.id).includes(this.search)
      );
    }
  },
  props: {
    collapsed: {
      type: Boolean,
      default: false
    }
  },
  mounted() {
    this.getTableData();
  },
  methods: {
    getTableData() {
      this.getAllmovies();
      this.getMovieTypes();
      this.getDirectorIds();
      this.getActorIds();
    },
    getmovieExcel() {
      axios.get(api.apigetmovieExcel, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        },
        responseType: "blob"
        // 关键
      }).then((response) => {
        const blob = new Blob([response.data], {
          type: "application/vnd.ms-excel"
        });
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement("a");
        a.href = url;
        a.download = "电影数据.xlsx";
        document.body.appendChild(a);
        a.click();
        window.URL.revokeObjectURL(url);
        document.body.removeChild(a);
      });
    },
    getAllmovies() {
      axios.get(api.apigetallmovie, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      }).then((response) => {
        if (response.data.code === 200) {
          this.moviesData = response.data.data;
          this.tableData = this.moviesData.map((item) => {
            return {
              id: item.id,
              title: item.title,
              description: item.description,
              score: item.score,
              movieTypes: item.movieTypes,
              isVip: item.isVip,
              directors: item.directors,
              actors: item.actors
            };
          });
          for (let i = 0; i < this.tableData.length; i++) {
            this.tableData[i].movieTypes = this.tableData[i].movieTypes.map((item) => item.name).join("/");
            this.tableData[i].directors = this.tableData[i].directors.map((item) => item.name).join("、");
            this.tableData[i].actors = this.tableData[i].actors.map((item) => item.name).join("、");
            this.tableData[i].description = this.tableData[i].description.length > 100 ? this.tableData[i].description.slice(
              0,
              100
            ) + "..." : this.tableData[i].description;
          }
        } else {
          this.$message.error("获取用户数据失败");
        }
      });
    },
    // 获取电影类型
    getMovieTypes() {
      axios.get(api.apigetallmovieType, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      }).then((response) => {
        if (response.data.code === 200) {
          this.movieTypes = response.data.data;
        } else {
          this.$message.error("获取电影类型失败");
        }
      });
    },
    getDirectorIds() {
      axios.get(api.apigetalldirector, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      }).then((response) => {
        if (response.data.code === 200) {
          this.directorsData = response.data.data;
        } else {
          this.$message.error("获取导演数据失败");
        }
      });
    },
    getActorIds() {
      axios.get(api.apigetallActor, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      }).then((response) => {
        if (response.data.code === 200) {
          this.actorsData = response.data.data;
        } else {
          this.$message.error("获取演员数据失败");
        }
      });
    },
    handleImport(file) {
      this.importLoading = true;
      const formData = new FormData();
      formData.append("file", file);
      axios.post(api.apiimportmovie, formData, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      }).then((response) => {
        if (response.data.code === 200) {
          ElMessage.success("导入成功");
          this.getTableData();
        } else {
          ElMessage.error("导入失败");
        }
      }).finally(() => {
        this.importLoading = false;
      });
      return false;
    },
    handleEdit(index, row) {
      this.editForm = { ...row };
      const ids = row.id - 1;
      this.editForm.description = this.moviesData[ids].description;
      this.editForm.movieTypes = this.moviesData[ids].movieTypes.map(
        (item) => item.id
      );
      this.editForm.directors = this.moviesData[ids].directors.map(
        (item) => item.id
      );
      this.editForm.actors = this.moviesData[ids].actors.map(
        (item) => item.id
      );
      this.editDialogVisible = true;
    },
    async handleEditSave() {
      let updataform = {
        id: this.editForm.id,
        title: this.editForm.title,
        description: this.editForm.description,
        directorIds: this.editForm.directors,
        actorId: this.editForm.actors,
        score: this.editForm.score,
        movieTypeIds: this.editForm.movieTypes,
        isVip: this.editForm.isVip
      };
      for (let i = 0; i < updataform.directorIds.length; i++) {
        if (typeof updataform.directorIds[i] === "string") {
          updataform.directorIds[i] = await this.createDirector(
            updataform.directorIds[i]
          );
        }
      }
      for (let i = 0; i < updataform.actorId.length; i++) {
        if (typeof updataform.actorId[i] === "string") {
          updataform.actorId[i] = await this.createActor(
            updataform.actorId[i]
          );
        }
      }
      for (let i = 0; i < updataform.movieTypeIds.length; i++) {
        if (typeof updataform.movieTypeIds[i] === "string") {
          updataform.movieTypeIds[i] = await this.createmovieTypes(
            updataform.movieTypeIds[i]
          );
        }
      }
      axios.put(api.apiupdatemovie, updataform, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      }).then((response) => {
        if (response.data.code === 200) {
          ElMessage.success("修改成功");
          console.log(response.data);
          this.getTableData();
        } else {
          ElMessage.error("修改失败");
        }
        this.editDialogVisible = false;
      });
    },
    createDirector(name) {
      return axios.post(
        api.apicreatedirector,
        { name },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`
          }
        }
      ).then((response) => {
        if (response.data.code === 200) {
          return response.data.data.id;
        } else {
          return null;
        }
      }).catch(() => {
        return null;
      });
    },
    createActor(name) {
      return axios.post(
        api.apicreateActor,
        { name },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`
          }
        }
      ).then((response) => {
        if (response.data.code === 200) {
          return response.data.data.id;
        } else {
          return null;
        }
      }).catch(() => {
        return null;
      });
    },
    createmovieTypes(name) {
      return axios.post(
        api.apicreatemovieType,
        { name },
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`
          }
        }
      ).then((response) => {
        if (response.data.code === 200) {
          return response.data.data.id;
        } else {
          return null;
        }
      }).catch(() => {
        return null;
      });
    },
    // 删除电影
    handleDelete(index, row) {
      ElMessageBox.confirm(`确定要删除电影 ${row.title} 吗？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.tableData.splice(index, 1);
        axios.delete(`${api.apideletemovie}/${row.id}`, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`
          }
        }).then((response) => {
          if (response.data.code === 200) {
            ElMessage.success("删除成功");
          } else {
            ElMessage.error("删除失败");
          }
        }).catch(() => {
        });
      }).catch(() => {
      });
    },
    addmovies() {
      this.addDialogVisible = true;
    },
    async handleAddSave() {
      const addform = {
        title: this.addForm.title,
        description: this.addForm.description,
        score: Number(this.addForm.score),
        movieTypeIds: this.addForm.movieTypeIds,
        isVip: this.addForm.isVip,
        directorIds: this.addForm.directorIds,
        actors: this.addForm.actors
      };
      for (let i = 0; i < addform.directorIds.length; i++) {
        if (typeof addform.directorIds[i] === "string") {
          addform.directorIds[i] = await this.createDirector(
            addform.directorIds[i]
          );
        }
      }
      for (let i = 0; i < addform.actors.length; i++) {
        if (typeof addform.actors[i] === "string") {
          addform.actors[i] = await this.createActor(
            addform.actors[i]
          );
        }
      }
      for (let i = 0; i < addform.movieTypeIds.length; i++) {
        if (typeof addform.movieTypeIds[i] === "string") {
          addform.movieTypeIds[i] = await this.createmovieTypes(
            addform.movieTypeIds[i]
          );
        }
      }
      console.log(addform);
      axios.post(api.apicreatemovie, addform, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      }).then((response) => {
        if (response.data.code === 200) {
          ElMessage.success("添加成功");
          this.addForm = {
            title: "",
            score: "",
            description: "",
            movieTypes: "",
            isVip: ""
          };
          this.getTableData();
        } else {
          ElMessage.error("添加失败");
        }
        this.addDialogVisible = false;
      });
    }
  }
};
const _hoisted_1 = { class: "adhome" };
const _hoisted_2 = { class: "adhome-stats" };
const _hoisted_3 = { class: "adhome-content" };
const _hoisted_4 = { class: "adhome-table-controller" };
const _hoisted_5 = {
  slot: "footer",
  class: "dialog-footer"
};
const _hoisted_6 = {
  slot: "footer",
  class: "dialog-footer"
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_el_row = resolveComponent("el-row");
  const _component_el_button = resolveComponent("el-button");
  const _component_el_upload = resolveComponent("el-upload");
  const _component_el_col = resolveComponent("el-col");
  const _component_DocumentCopy = resolveComponent("DocumentCopy");
  const _component_el_icon = resolveComponent("el-icon");
  const _component_el_tooltip = resolveComponent("el-tooltip");
  const _component_el_table_column = resolveComponent("el-table-column");
  const _component_el_input = resolveComponent("el-input");
  const _component_el_table = resolveComponent("el-table");
  const _component_el_form_item = resolveComponent("el-form-item");
  const _component_el_select_v2 = resolveComponent("el-select-v2");
  const _component_el_option = resolveComponent("el-option");
  const _component_el_select = resolveComponent("el-select");
  const _component_el_form = resolveComponent("el-form");
  const _component_el_dialog = resolveComponent("el-dialog");
  const _component_el_card = resolveComponent("el-card");
  const _directive_loading = resolveDirective("loading");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createBaseVNode("div", _hoisted_2, [
      createVNode(_component_el_row, null, {
        default: withCtx(() => _cache[20] || (_cache[20] = [
          createBaseVNode("p", { class: "Adtitle1" }, "电影时刻   /", -1),
          createBaseVNode("p", { class: "Adtitle2" }, "  电影管理", -1)
        ])),
        _: 1
      }),
      createVNode(_component_el_row, null, {
        default: withCtx(() => _cache[21] || (_cache[21] = [
          createBaseVNode("p", { class: "Adtitle3" }, "电影管理", -1)
        ])),
        _: 1
      })
    ]),
    withDirectives((openBlock(), createElementBlock("div", _hoisted_3, [
      createBaseVNode("div", _hoisted_4, [
        createVNode(_component_el_card, {
          class: "adhome-card",
          shadow: "always",
          style: normalizeStyle({ width: $props.collapsed ? "95.1vw" : "87.9vw" })
        }, {
          default: withCtx(() => [
            createVNode(_component_el_row, null, {
              default: withCtx(() => [
                createVNode(_component_el_col, { span: 2 }, {
                  default: withCtx(() => [
                    createVNode(_component_el_upload, {
                      "show-file-list": false,
                      accept: ".xlsx,.xls",
                      "before-upload": $options.handleImport
                    }, {
                      default: withCtx(() => [
                        createVNode(_component_el_button, {
                          type: "success",
                          round: ""
                        }, {
                          default: withCtx(() => _cache[22] || (_cache[22] = [
                            createTextVNode(" 导入电影 ")
                          ])),
                          _: 1
                        })
                      ]),
                      _: 1
                    }, 8, ["before-upload"])
                  ]),
                  _: 1
                }),
                createVNode(_component_el_col, { span: 21 }, {
                  default: withCtx(() => [
                    createVNode(_component_el_button, {
                      type: "success",
                      onClick: $options.addmovies,
                      round: ""
                    }, {
                      default: withCtx(() => _cache[23] || (_cache[23] = [
                        createTextVNode(" 添加电影 ")
                      ])),
                      _: 1
                    }, 8, ["onClick"])
                  ]),
                  _: 1
                }),
                createVNode(_component_el_col, { span: 1 }, {
                  default: withCtx(() => [
                    createVNode(_component_el_tooltip, {
                      content: "导出电影Excel报表",
                      placement: "top"
                    }, {
                      default: withCtx(() => [
                        createVNode(_component_el_button, {
                          type: "success",
                          circle: "",
                          onClick: $options.getmovieExcel
                        }, {
                          default: withCtx(() => [
                            createVNode(_component_el_icon, null, {
                              default: withCtx(() => [
                                createVNode(_component_DocumentCopy)
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        }, 8, ["onClick"])
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                })
              ]),
              _: 1
            }),
            createVNode(_component_el_table, {
              data: $options.filterTableData,
              style: { "width": "100%", "margin-top": "40px" },
              "max-height": "550px"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_table_column, {
                  label: "id号",
                  prop: "id",
                  align: "center"
                }),
                createVNode(_component_el_table_column, {
                  label: "电影名称",
                  prop: "title"
                }),
                createVNode(_component_el_table_column, {
                  label: "电影描述",
                  prop: "description",
                  align: "center",
                  width: "350"
                }),
                createVNode(_component_el_table_column, {
                  label: "导演",
                  prop: "directors",
                  align: "center"
                }),
                createVNode(_component_el_table_column, {
                  label: "演员",
                  prop: "actors",
                  align: "center"
                }),
                createVNode(_component_el_table_column, {
                  label: "电影类型",
                  prop: "movieTypes"
                }),
                createVNode(_component_el_table_column, {
                  label: "评分",
                  prop: "score",
                  align: "center"
                }),
                createVNode(_component_el_table_column, {
                  label: "权限",
                  prop: "isVip",
                  formatter: (row) => row.isVip === 1 ? "VIP电影" : "普通电影"
                }, null, 8, ["formatter"]),
                createVNode(_component_el_table_column, { align: "right" }, {
                  header: withCtx(() => [
                    createVNode(_component_el_input, {
                      modelValue: $data.search,
                      "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.search = $event),
                      size: "small",
                      placeholder: "搜索电影"
                    }, null, 8, ["modelValue"])
                  ]),
                  default: withCtx((scope) => [
                    createVNode(_component_el_button, {
                      size: "small",
                      type: "primary",
                      onClick: ($event) => $options.handleEdit(scope.$index, scope.row)
                    }, {
                      default: withCtx(() => _cache[24] || (_cache[24] = [
                        createTextVNode(" 编辑 ")
                      ])),
                      _: 2
                    }, 1032, ["onClick"]),
                    createVNode(_component_el_button, {
                      size: "small",
                      type: "danger",
                      onClick: ($event) => $options.handleDelete(scope.$index, scope.row)
                    }, {
                      default: withCtx(() => _cache[25] || (_cache[25] = [
                        createTextVNode(" 删除 ")
                      ])),
                      _: 2
                    }, 1032, ["onClick"])
                  ]),
                  _: 1
                })
              ]),
              _: 1
            }, 8, ["data"]),
            createVNode(_component_el_dialog, {
              title: "添加电影",
              modelValue: $data.addDialogVisible,
              "onUpdate:modelValue": _cache[9] || (_cache[9] = ($event) => $data.addDialogVisible = $event)
            }, {
              default: withCtx(() => [
                createVNode(_component_el_form, {
                  model: $data.addForm,
                  "label-width": "80px"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_form_item, { label: "电影名称" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.addForm.title,
                          "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => $data.addForm.title = $event),
                          placeholder: "请输入电影名称"
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "电影描述" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.addForm.description,
                          "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => $data.addForm.description = $event),
                          placeholder: "请输入电影描述",
                          type: "textarea"
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "导演" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select_v2, {
                          modelValue: $data.addForm.directorIds,
                          "onUpdate:modelValue": _cache[3] || (_cache[3] = ($event) => $data.addForm.directorIds = $event),
                          placeholder: "请选择导演",
                          "tag-type": "success",
                          multiple: "",
                          filterable: "",
                          "allow-create": "",
                          options: $data.directorsData,
                          props: { label: "name", value: "id" }
                        }, null, 8, ["modelValue", "options"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "演员" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select_v2, {
                          modelValue: $data.addForm.actors,
                          "onUpdate:modelValue": _cache[4] || (_cache[4] = ($event) => $data.addForm.actors = $event),
                          placeholder: "请选择演员",
                          "tag-type": "success",
                          multiple: "",
                          filterable: "",
                          "allow-create": "",
                          options: $data.actorsData,
                          props: { label: "name", value: "id" }
                        }, null, 8, ["modelValue", "options"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "电影类型" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select_v2, {
                          modelValue: $data.addForm.movieTypeIds,
                          "onUpdate:modelValue": _cache[5] || (_cache[5] = ($event) => $data.addForm.movieTypeIds = $event),
                          placeholder: "请选择电影类型",
                          "tag-type": "success",
                          multiple: "",
                          filterable: "",
                          "allow-create": "",
                          options: $data.movieTypes,
                          props: { label: "name", value: "id" }
                        }, null, 8, ["modelValue", "options"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "评分" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.addForm.score,
                          "onUpdate:modelValue": _cache[6] || (_cache[6] = ($event) => $data.addForm.score = $event),
                          placeholder: "请输入评分"
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "权限" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select, {
                          modelValue: $data.addForm.isVip,
                          "onUpdate:modelValue": _cache[7] || (_cache[7] = ($event) => $data.addForm.isVip = $event),
                          placeholder: "请选择权限"
                        }, {
                          default: withCtx(() => [
                            createVNode(_component_el_option, {
                              label: "普通电影",
                              value: 0
                            }),
                            createVNode(_component_el_option, {
                              label: "VIP电影",
                              value: 1
                            })
                          ]),
                          _: 1
                        }, 8, ["modelValue"])
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                }, 8, ["model"]),
                createBaseVNode("div", _hoisted_5, [
                  createVNode(_component_el_button, {
                    onClick: _cache[8] || (_cache[8] = ($event) => $data.addDialogVisible = false)
                  }, {
                    default: withCtx(() => _cache[26] || (_cache[26] = [
                      createTextVNode("取 消")
                    ])),
                    _: 1
                  }),
                  createVNode(_component_el_button, {
                    type: "primary",
                    onClick: $options.handleAddSave
                  }, {
                    default: withCtx(() => _cache[27] || (_cache[27] = [
                      createTextVNode("确 定")
                    ])),
                    _: 1
                  }, 8, ["onClick"])
                ])
              ]),
              _: 1
            }, 8, ["modelValue"]),
            createVNode(_component_el_dialog, {
              title: "编辑电影信息",
              modelValue: $data.editDialogVisible,
              "onUpdate:modelValue": _cache[19] || (_cache[19] = ($event) => $data.editDialogVisible = $event)
            }, {
              default: withCtx(() => [
                createVNode(_component_el_form, {
                  model: $data.editForm,
                  "label-width": "80px"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_form_item, { label: "id号" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.editForm.id,
                          "onUpdate:modelValue": _cache[10] || (_cache[10] = ($event) => $data.editForm.id = $event),
                          disabled: ""
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "电影名称" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.editForm.title,
                          "onUpdate:modelValue": _cache[11] || (_cache[11] = ($event) => $data.editForm.title = $event),
                          placeholder: "请输入电影名称"
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "电影描述" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.editForm.description,
                          "onUpdate:modelValue": _cache[12] || (_cache[12] = ($event) => $data.editForm.description = $event),
                          placeholder: "请输入电影描述",
                          type: "textarea"
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "导演" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select_v2, {
                          modelValue: $data.editForm.directors,
                          "onUpdate:modelValue": _cache[13] || (_cache[13] = ($event) => $data.editForm.directors = $event),
                          placeholder: "请选择导演",
                          "tag-type": "success",
                          multiple: "",
                          filterable: "",
                          "allow-create": "",
                          options: $data.directorsData,
                          props: { label: "name", value: "id" }
                        }, null, 8, ["modelValue", "options"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "演员" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select_v2, {
                          modelValue: $data.editForm.actors,
                          "onUpdate:modelValue": _cache[14] || (_cache[14] = ($event) => $data.editForm.actors = $event),
                          placeholder: "请选择演员",
                          "tag-type": "success",
                          multiple: "",
                          filterable: "",
                          "allow-create": "",
                          options: $data.actorsData,
                          props: { label: "name", value: "id" }
                        }, null, 8, ["modelValue", "options"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "电影类型" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select_v2, {
                          modelValue: $data.editForm.movieTypes,
                          "onUpdate:modelValue": _cache[15] || (_cache[15] = ($event) => $data.editForm.movieTypes = $event),
                          placeholder: "请选择电影类型",
                          "tag-type": "success",
                          multiple: "",
                          filterable: "",
                          "allow-create": "",
                          options: $data.movieTypes,
                          props: { label: "name", value: "id" }
                        }, null, 8, ["modelValue", "options"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "评分" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.editForm.score,
                          "onUpdate:modelValue": _cache[16] || (_cache[16] = ($event) => $data.editForm.score = $event),
                          placeholder: "请输入评分"
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "权限" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select, {
                          modelValue: $data.editForm.isVip,
                          "onUpdate:modelValue": _cache[17] || (_cache[17] = ($event) => $data.editForm.isVip = $event),
                          placeholder: "请选择权限"
                        }, {
                          default: withCtx(() => [
                            createVNode(_component_el_option, {
                              label: "普通电影",
                              value: 0
                            }),
                            createVNode(_component_el_option, {
                              label: "VIP电影",
                              value: 1
                            })
                          ]),
                          _: 1
                        }, 8, ["modelValue"])
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                }, 8, ["model"]),
                createBaseVNode("div", _hoisted_6, [
                  createVNode(_component_el_button, {
                    onClick: _cache[18] || (_cache[18] = ($event) => $data.editDialogVisible = false)
                  }, {
                    default: withCtx(() => _cache[28] || (_cache[28] = [
                      createTextVNode("取 消")
                    ])),
                    _: 1
                  }),
                  createVNode(_component_el_button, {
                    type: "primary",
                    onClick: $options.handleEditSave
                  }, {
                    default: withCtx(() => _cache[29] || (_cache[29] = [
                      createTextVNode("确 定")
                    ])),
                    _: 1
                  }, 8, ["onClick"])
                ])
              ]),
              _: 1
            }, 8, ["modelValue"])
          ]),
          _: 1
        }, 8, ["style"])
      ])
    ])), [
      [_directive_loading, $data.importLoading]
    ])
  ]);
}
const Admoviecontroller = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render]]);
export {
  Admoviecontroller as default
};
