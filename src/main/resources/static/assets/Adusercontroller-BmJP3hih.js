/* empty css                */
import { _ as _export_sfc, c as createElementBlock, b as createBaseVNode, a as createVNode, w as withCtx, r as resolveComponent, n as normalizeStyle, E as ElMessage, e as axios, f as api, i as ElMessageBox, o as openBlock, g as createTextVNode, t as toDisplayString } from "./index-Zb4gIbIS.js";
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
        username: "",
        password: "",
        email: "",
        userType: "",
        status: 1
      },
      editForm: {
        id: "",
        username: "",
        email: "",
        userType: "",
        status: 1
      }
    };
  },
  components: {},
  computed: {
    filterTableData() {
      if (!this.search) return this.tableData;
      return this.tableData.filter(
        (item) => item.username.includes(this.search) || String(item.id).includes(this.search)
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
      axios.get(api.apigetalluser, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      }).then((response) => {
        if (response.data.code === 200) {
          this.tableData = response.data.data;
        } else {
          this.$message.error("获取用户数据失败");
        }
      });
    },
    handleEditSave() {
      const idx = this.tableData.findIndex(
        (u) => u.id === this.editForm.id
      );
      if (idx !== -1) {
        this.tableData[idx] = { ...this.editForm };
      }
      const updataform = {
        id: this.editForm.id,
        username: this.editForm.username,
        email: this.editForm.email,
        userType: this.editForm.userType,
        status: this.editForm.status
      };
      axios.post(api.apiupdateuser, updataform, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      }).then((response) => {
        if (response.data.code === 200) {
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
      ElMessageBox.confirm(
        `确定要删除用户 ${row.username} 吗？`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      ).then(() => {
        this.tableData.splice(index, 1);
        axios.post(
          api.apideleteuser + "?uid=" + row.id,
          {},
          {
            headers: {
              Authorization: `Bearer ${localStorage.getItem("token")}`
            }
          }
        ).then((response) => {
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
      this.addForm = {
        username: "",
        password: "",
        email: "",
        userType: "",
        status: null
      };
      this.addDialogVisible = true;
    },
    handleAddSave() {
      if (!this.addForm.username || this.addForm.username.length < 3) {
        ElMessage.error("用户名至少3个字符");
        return;
      }
      if (!this.addForm.password || this.addForm.password.length < 5) {
        ElMessage.error("密码至少5个字符");
        return;
      }
      const addform = {
        username: this.addForm.username,
        password: this.addForm.password,
        email: this.addForm.email,
        userType: this.addForm.userType,
        status: this.addForm.status
      };
      axios.post(api.apicreateuser, addform, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("token")}`
        }
      }).then((response) => {
        if (response.data.code === 200) {
          this.getTableData();
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
  const _component_el_tag = resolveComponent("el-tag");
  const _component_el_input = resolveComponent("el-input");
  const _component_el_table = resolveComponent("el-table");
  const _component_el_form_item = resolveComponent("el-form-item");
  const _component_el_option = resolveComponent("el-option");
  const _component_el_select = resolveComponent("el-select");
  const _component_el_form = resolveComponent("el-form");
  const _component_el_dialog = resolveComponent("el-dialog");
  const _component_el_card = resolveComponent("el-card");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createBaseVNode("div", _hoisted_2, [
      createVNode(_component_el_row, null, {
        default: withCtx(() => _cache[15] || (_cache[15] = [
          createBaseVNode("p", { class: "Adtitle1" }, "电影时刻   /", -1),
          createBaseVNode("p", { class: "Adtitle2" }, "  用户管理", -1)
        ])),
        _: 1
      }),
      createVNode(_component_el_row, null, {
        default: withCtx(() => _cache[16] || (_cache[16] = [
          createBaseVNode("p", { class: "Adtitle3" }, "用户管理", -1)
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
                  default: withCtx(() => _cache[17] || (_cache[17] = [
                    createTextVNode(" 添加用户 ")
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
                  label: "用户名",
                  prop: "username"
                }),
                createVNode(_component_el_table_column, {
                  label: "邮箱",
                  prop: "email"
                }),
                createVNode(_component_el_table_column, {
                  label: "用户类型",
                  prop: "userType",
                  formatter: (row) => row.userType === 1 ? "会员" : "普通用户"
                }, null, 8, ["formatter"]),
                createVNode(_component_el_table_column, {
                  label: "状态",
                  prop: "status"
                }, {
                  default: withCtx((scope) => [
                    createVNode(_component_el_tag, {
                      type: scope.row.status === 1 ? "success" : "danger",
                      size: "default",
                      effect: "dark"
                    }, {
                      default: withCtx(() => [
                        createTextVNode(toDisplayString(scope.row.status === 1 ? "正常" : "已禁用"), 1)
                      ]),
                      _: 2
                    }, 1032, ["type"])
                  ]),
                  _: 1
                }),
                createVNode(_component_el_table_column, { align: "right" }, {
                  header: withCtx(() => [
                    createVNode(_component_el_input, {
                      modelValue: $data.search,
                      "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.search = $event),
                      size: "small",
                      placeholder: "搜索用户"
                    }, null, 8, ["modelValue"])
                  ]),
                  default: withCtx((scope) => [
                    createVNode(_component_el_button, {
                      size: "small",
                      type: "primary",
                      onClick: ($event) => $options.handleEdit(scope.$index, scope.row)
                    }, {
                      default: withCtx(() => _cache[18] || (_cache[18] = [
                        createTextVNode(" 编辑 ")
                      ])),
                      _: 2
                    }, 1032, ["onClick"]),
                    createVNode(_component_el_button, {
                      size: "small",
                      type: "danger",
                      onClick: ($event) => $options.handleDelete(scope.$index, scope.row)
                    }, {
                      default: withCtx(() => _cache[19] || (_cache[19] = [
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
              title: "添加用户",
              modelValue: $data.addDialogVisible,
              "onUpdate:modelValue": _cache[7] || (_cache[7] = ($event) => $data.addDialogVisible = $event)
            }, {
              default: withCtx(() => [
                createVNode(_component_el_form, {
                  model: $data.addForm,
                  "label-width": "80px"
                }, {
                  default: withCtx(() => [
                    createVNode(_component_el_form_item, { label: "用户名" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.addForm.username,
                          "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => $data.addForm.username = $event)
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "密码" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.addForm.password,
                          "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => $data.addForm.password = $event),
                          type: "password"
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "邮箱" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.addForm.email,
                          "onUpdate:modelValue": _cache[3] || (_cache[3] = ($event) => $data.addForm.email = $event)
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "用户类型" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select, {
                          modelValue: $data.addForm.userType,
                          "onUpdate:modelValue": _cache[4] || (_cache[4] = ($event) => $data.addForm.userType = $event)
                        }, {
                          default: withCtx(() => [
                            createVNode(_component_el_option, {
                              label: "普通用户",
                              value: 0
                            }),
                            createVNode(_component_el_option, {
                              label: "会员",
                              value: 1
                            })
                          ]),
                          _: 1
                        }, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "状态" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select, {
                          modelValue: $data.addForm.status,
                          "onUpdate:modelValue": _cache[5] || (_cache[5] = ($event) => $data.addForm.status = $event)
                        }, {
                          default: withCtx(() => [
                            createVNode(_component_el_option, {
                              label: "正常",
                              value: 1
                            }),
                            createVNode(_component_el_option, {
                              label: "禁用",
                              value: 0
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
                    onClick: _cache[6] || (_cache[6] = ($event) => $data.addDialogVisible = false)
                  }, {
                    default: withCtx(() => _cache[20] || (_cache[20] = [
                      createTextVNode("取 消")
                    ])),
                    _: 1
                  }),
                  createVNode(_component_el_button, {
                    type: "primary",
                    onClick: $options.handleAddSave
                  }, {
                    default: withCtx(() => _cache[21] || (_cache[21] = [
                      createTextVNode("确 定")
                    ])),
                    _: 1
                  }, 8, ["onClick"])
                ])
              ]),
              _: 1
            }, 8, ["modelValue"]),
            createVNode(_component_el_dialog, {
              title: "编辑用户信息",
              modelValue: $data.editDialogVisible,
              "onUpdate:modelValue": _cache[14] || (_cache[14] = ($event) => $data.editDialogVisible = $event)
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
                          "onUpdate:modelValue": _cache[8] || (_cache[8] = ($event) => $data.editForm.id = $event),
                          disabled: ""
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "用户名" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.editForm.username,
                          "onUpdate:modelValue": _cache[9] || (_cache[9] = ($event) => $data.editForm.username = $event)
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "邮箱" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_input, {
                          modelValue: $data.editForm.email,
                          "onUpdate:modelValue": _cache[10] || (_cache[10] = ($event) => $data.editForm.email = $event)
                        }, null, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "用户类型" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select, {
                          modelValue: $data.editForm.userType,
                          "onUpdate:modelValue": _cache[11] || (_cache[11] = ($event) => $data.editForm.userType = $event)
                        }, {
                          default: withCtx(() => [
                            createVNode(_component_el_option, {
                              label: "普通用户",
                              value: 0
                            }),
                            createVNode(_component_el_option, {
                              label: "会员",
                              value: 1
                            })
                          ]),
                          _: 1
                        }, 8, ["modelValue"])
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_form_item, { label: "状态" }, {
                      default: withCtx(() => [
                        createVNode(_component_el_select, {
                          modelValue: $data.editForm.status,
                          "onUpdate:modelValue": _cache[12] || (_cache[12] = ($event) => $data.editForm.status = $event)
                        }, {
                          default: withCtx(() => [
                            createVNode(_component_el_option, {
                              label: "正常",
                              value: 1
                            }),
                            createVNode(_component_el_option, {
                              label: "禁用",
                              value: 0
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
                    onClick: _cache[13] || (_cache[13] = ($event) => $data.editDialogVisible = false)
                  }, {
                    default: withCtx(() => _cache[22] || (_cache[22] = [
                      createTextVNode("取 消")
                    ])),
                    _: 1
                  }),
                  createVNode(_component_el_button, {
                    type: "primary",
                    onClick: $options.handleEditSave
                  }, {
                    default: withCtx(() => _cache[23] || (_cache[23] = [
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
const Adusercontroller = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render]]);
export {
  Adusercontroller as default
};
