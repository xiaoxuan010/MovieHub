/* empty css                */
import { _ as _export_sfc, c as createElementBlock, b as createBaseVNode, a as createVNode, w as withCtx, r as resolveComponent, q as normalizeStyle, e as axios, f as api, E as ElMessage, i as ElMessageBox, o as openBlock, g as createTextVNode } from "./index-BHtkpZle.js";
const _sfc_main = {
  name: "Adusercontroller",
  data() {
    return {
      search: "",
      // 搜索关键字
      tableData: [],
      editDialogVisible: false,
      // 控制编辑弹窗显示
      addDialogVisible: false,
      // 控制添加弹窗显示
      addForm: {
        name: "",
        description: ""
      },
      editForm: {
        id: "",
        name: "",
        description: ""
      }
    };
  },
  components: {},
  computed: {
    filterTableData() {
      if (!this.search) return this.tableData;
      return this.tableData.filter(
        (item) => item.name.includes(this.search) || String(item.id).includes(this.search)
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
      axios.get(api.apigetallActor, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      }).then((response) => {
        this.tableData = response.data.data;
      });
    },
    handleEditSave() {
      const updataform = {
        id: this.editForm.id,
        name: this.editForm.name,
        description: this.editForm.description
      };
      axios.put(api.apiupdateActor(this.editForm.id), updataform, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      }).then((response) => {
        if (response.data.code === 200) {
          this.getTableData();
          ElMessage.success("修改成功");
        } else {
          ElMessage.error("修改失败");
        }
        this.editDialogVisible = false;
      });
    },
    handleEdit(index, row) {
      this.editForm = { ...row };
      this.editDialogVisible = true;
    },
    handleDelete(index, row) {
      ElMessageBox.confirm(`确定要删除此演员 ${row.name} 吗？`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.tableData.splice(index, 1);
        axios.delete(
          api.apideleteActor(row.id),
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`
            }
          }
        ).then((response) => {
          console.log(response);
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
    handleAdd() {
      this.addDialogVisible = true;
    },
    handleAddSave() {
      const addform = {
        name: this.addForm.name,
        description: this.addForm.description
      };
      axios.post(api.apicreateActor, addform, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      }).then((response) => {
        if (response.data.code === 200) {
          this.getTableData();
          this.addForm.name = "";
          this.addForm.description = "";
          ElMessage.success("添加成功");
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
const _hoisted_4 = { class: "adhome-controller" };
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
  const _component_el_table_column = resolveComponent("el-table-column");
  const _component_el_input = resolveComponent("el-input");
  const _component_el_table = resolveComponent("el-table");
  const _component_el_form_item = resolveComponent("el-form-item");
  const _component_el_form = resolveComponent("el-form");
  const _component_el_dialog = resolveComponent("el-dialog");
  const _component_el_card = resolveComponent("el-card");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createBaseVNode("div", _hoisted_2, [
      createVNode(_component_el_row, null, {
        default: withCtx(() => _cache[10] || (_cache[10] = [
          createBaseVNode("p", { class: "Adtitle1" }, "电影时刻   /", -1),
          createBaseVNode("p", { class: "Adtitle2" }, "  演员管理", -1)
        ])),
        _: 1
      }),
      createVNode(_component_el_row, null, {
        default: withCtx(() => _cache[11] || (_cache[11] = [
          createBaseVNode("p", { class: "Adtitle3" }, "演员管理", -1)
        ])),
        _: 1
      })
    ]),
    createBaseVNode("div", _hoisted_3, [
      createBaseVNode("div", _hoisted_4, [
        createVNode(_component_el_card, {
          class: "adhome-card",
          shadow: "always",
          style: normalizeStyle({ width: $props.collapsed ? "95.1vw" : "87.9vw" })
        }, {
          default: withCtx(() => [
            createVNode(_component_el_row, null, {
              default: withCtx(() => [
                createVNode(_component_el_button, {
                  type: "success",
                  onClick: $options.handleAdd,
                  round: ""
                }, {
                  default: withCtx(() => _cache[12] || (_cache[12] = [
                    createTextVNode(" 添加演员 ")
                  ])),
                  _: 1
                }, 8, ["onClick"])
              ]),
              _: 1
            }),
            createVNode(_component_el_table, {
              data: $options.filterTableData,
              style: { "width": "100%", "margin-top": "40px" },
              height: "550px"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_table_column, {
                  label: "id号",
                  prop: "id"
                }),
                createVNode(_component_el_table_column, {
                  label: "姓名",
                  prop: "name"
                }),
                createVNode(_component_el_table_column, {
                  label: "描述",
                  prop: "description"
                }),
                createVNode(_component_el_table_column, { align: "right" }, {
                  header: withCtx(() => [
                    createVNode(_component_el_input, {
                      modelValue: $data.search,
                      "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.search = $event),
                      size: "small",
                      placeholder: "搜索演员"
                    }, null, 8, ["modelValue"])
                  ]),
                  default: withCtx((scope) => [
                    createVNode(_component_el_button, {
                      size: "small",
                      type: "primary",
                      onClick: ($event) => $options.handleEdit(scope.$index, scope.row)
                    }, {
                      default: withCtx(() => _cache[13] || (_cache[13] = [
                        createTextVNode(" 编辑 ")
                      ])),
                      _: 2
                    }, 1032, ["onClick"]),
                    createVNode(_component_el_button, {
                      size: "small",
                      type: "danger",
                      onClick: ($event) => $options.handleDelete(scope.$index, scope.row)
                    }, {
                      default: withCtx(() => _cache[14] || (_cache[14] = [
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
              title: "添加演员信息",
              modelValue: $data.addDialogVisible,
              "onUpdate:modelValue": _cache[4] || (_cache[4] = ($event) => $data.addDialogVisible = $event)
            }, {
              default: withCtx(() => [
                createVNode(_component_el_form, {
                  model: $data.addForm,
                  "label-width": "80px"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_form_item, { label: "姓名" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.addForm.name,
                          "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => $data.addForm.name = $event)
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "描述" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          type: "textarea",
                          modelValue: $data.addForm.description,
                          "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => $data.addForm.description = $event)
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                }, 8, ["model"]),
                createBaseVNode("div", _hoisted_5, [
                  createVNode(_component_el_button, {
                    onClick: _cache[3] || (_cache[3] = ($event) => $data.addDialogVisible = false)
                  }, {
                    default: withCtx(() => _cache[15] || (_cache[15] = [
                      createTextVNode("取 消")
                    ])),
                    _: 1
                  }),
                  createVNode(_component_el_button, {
                    type: "primary",
                    onClick: $options.handleAddSave
                  }, {
                    default: withCtx(() => _cache[16] || (_cache[16] = [
                      createTextVNode("确 定")
                    ])),
                    _: 1
                  }, 8, ["onClick"])
                ])
              ]),
              _: 1
            }, 8, ["modelValue"]),
            createVNode(_component_el_dialog, {
              title: "编辑演员信息",
              modelValue: $data.editDialogVisible,
              "onUpdate:modelValue": _cache[9] || (_cache[9] = ($event) => $data.editDialogVisible = $event)
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
                          "onUpdate:modelValue": _cache[5] || (_cache[5] = ($event) => $data.editForm.id = $event),
                          disabled: ""
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "姓名" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.editForm.name,
                          "onUpdate:modelValue": _cache[6] || (_cache[6] = ($event) => $data.editForm.name = $event)
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "描述" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          type: "textarea",
                          modelValue: $data.editForm.description,
                          "onUpdate:modelValue": _cache[7] || (_cache[7] = ($event) => $data.editForm.description = $event)
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                }, 8, ["model"]),
                createBaseVNode("div", _hoisted_6, [
                  createVNode(_component_el_button, {
                    onClick: _cache[8] || (_cache[8] = ($event) => $data.editDialogVisible = false)
                  }, {
                    default: withCtx(() => _cache[17] || (_cache[17] = [
                      createTextVNode("取 消")
                    ])),
                    _: 1
                  }),
                  createVNode(_component_el_button, {
                    type: "primary",
                    onClick: $options.handleEditSave
                  }, {
                    default: withCtx(() => _cache[18] || (_cache[18] = [
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
    ])
  ]);
}
const Adactor = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render]]);
export {
  Adactor as default
};
