import { _ as _export_sfc, N as Navbar, c as createElementBlock, a as createVNode, b as createBaseVNode, r as resolveComponent, w as withCtx, e as axios, f as api, E as ElMessage, o as openBlock, t as toDisplayString, g as createTextVNode } from "./index-Zb4gIbIS.js";
const _imports_0 = "/puser.jpg";
const _sfc_main = {
  name: "User",
  components: {
    Navbar
  },
  data() {
    return {
      dialogVisible: false,
      // 控制对话框显示
      vipDialogVisible: false,
      // 控制VIP对话框显示
      editForm: {
        oldpassword: "",
        // 编辑的邮箱
        password: "",
        // 编辑的密码
        againpassword: ""
        // 编辑的确认密码
      },
      rules: {
        oldpassword: [
          {
            required: true,
            message: "请输入旧密码",
            trigger: ["change", "blur"]
          },
          {
            min: 5,
            max: 50,
            message: "长度在 5 到 50 个字符之间",
            trigger: ["change", "blur"]
          }
        ],
        password: [
          {
            required: true,
            message: "请输入新密码",
            trigger: ["change", "blur"]
          },
          {
            min: 5,
            max: 50,
            message: "长度在 5 到 50 个字符之间",
            trigger: ["change", "blur"]
          }
        ],
        againpassword: [
          {
            required: true,
            message: "请再次输入新密码",
            trigger: ["change", "blur"]
          },
          {
            min: 5,
            max: 50,
            message: "长度在 5 到 50 个字符之间",
            trigger: ["change", "blur"]
          }
        ]
      },
      isLoggedIn: true,
      // 登录状态
      token: localStorage.getItem("token"),
      // 假设使用localStorage存储token
      userInfo: {
        username: "",
        email: "",
        userType: 0
        // 0表示普通用户，1表示VIP用户
      }
    };
  },
  created() {
    this.getUserInfo();
  },
  methods: {
    getUserInfo() {
      axios.get(api.apigetUsermessage, {
        headers: {
          Authorization: "Bearer " + this.token
        }
      }).then((response) => {
        this.userInfo.username = response.data.data.username;
        this.userInfo.email = response.data.data.email;
        this.userInfo.userType = response.data.data.userType;
      }).catch((error) => {
        ElMessage({
          message: "获取用户信息失败",
          type: "error"
        });
      });
    },
    openDialog() {
      this.editForm.oldpassword = "";
      this.editForm.password = "";
      this.editForm.againpassword = "";
      this.dialogVisible = true;
    },
    handleSave() {
      this.$refs.editForm.validate((valid) => {
        if (!valid) {
          ElMessage({ message: "表单验证不通过", type: "error" });
          console.error("表单验证不通过");
          return false;
        } else {
          if (this.editForm.password !== this.editForm.againpassword) {
            console.error("新密码和确认密码不一致");
            ElMessage({
              message: "新密码和确认密码不一致",
              type: "error"
            });
            return;
          }
          axios.post(
            api.apiupdateUser,
            {
              oldPassword: this.editForm.oldpassword,
              newPassword: this.editForm.password
            },
            {
              headers: {
                Authorization: "Bearer " + this.token
              }
            }
          ).then((response) => {
            if (response.data.code !== 200) {
              ElMessage({
                message: response.data.message,
                type: "error"
              });
              this.openDialog();
              return;
            }
            ElMessage({
              message: "密码修改成功",
              type: "success"
            });
          }).catch((error) => {
            console.error("更新用户信息失败:", error);
          });
          this.dialogVisible = false;
        }
      });
    },
    returnhome() {
      localStorage.removeItem("token");
      localStorage.removeItem("userType");
      localStorage.removeItem("username");
      this.isLoggedIn = false;
      this.$router.push("/");
    },
    payVip(duration) {
      this.vipDialogVisible = false;
      axios.post(
        api.apitoVip,
        { duration },
        {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token")
          }
        }
      ).then((res) => {
        if (res.data.code === 200 && res.data.data.formHtml) {
          const payWindow = window.open("", "_blank");
          payWindow.document.write(res.data.data.formHtml);
          payWindow.document.close();
        } else {
          this.$message.error("支付请求失败");
        }
      });
    }
  }
};
const _hoisted_1 = { class: "usermaindemo user-page" };
const _hoisted_2 = { class: "usermaintext" };
const _hoisted_3 = { class: "details" };
const _hoisted_4 = { class: "text_item" };
const _hoisted_5 = { class: "text_item" };
const _hoisted_6 = { class: "text_item" };
const _hoisted_7 = { class: "text_item" };
const _hoisted_8 = { style: { "text-align": "center" } };
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_Navbar = resolveComponent("Navbar");
  const _component_el_button = resolveComponent("el-button");
  const _component_el_dialog = resolveComponent("el-dialog");
  const _component_el_input = resolveComponent("el-input");
  const _component_el_form_item = resolveComponent("el-form-item");
  const _component_el_form = resolveComponent("el-form");
  const _component_el_card = resolveComponent("el-card");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createVNode(_component_Navbar),
    createBaseVNode("div", _hoisted_2, [
      _cache[19] || (_cache[19] = createBaseVNode("img", {
        src: _imports_0,
        alt: "User Avatar",
        class: "uavatar"
      }, null, -1)),
      createBaseVNode("div", _hoisted_3, [
        createVNode(_component_el_card, { class: "box-card" }, {
          header: withCtx(() => _cache[9] || (_cache[9] = [
            createBaseVNode("div", { class: "umaintext" }, "个人信息", -1)
          ])),
          default: withCtx(() => [
            createBaseVNode("div", _hoisted_4, "用户名: " + toDisplayString($data.userInfo.username), 1),
            createBaseVNode("div", _hoisted_5, "邮箱: " + toDisplayString($data.userInfo.email), 1),
            createBaseVNode("div", _hoisted_6, " 用户类型: " + toDisplayString($data.userInfo.userType == 0 ? "普通用户" : "VIP用户"), 1),
            _cache[17] || (_cache[17] = createBaseVNode("div", { class: "text_item" }, " 个性签名：逃跑吧，跑出这满是世俗的生活 ", -1)),
            _cache[18] || (_cache[18] = createBaseVNode("div", { class: "text_item" }, "注册时间：2025-04-21", -1)),
            createBaseVNode("div", _hoisted_7, [
              createVNode(_component_el_button, {
                type: "success",
                onClick: _cache[0] || (_cache[0] = ($event) => $data.vipDialogVisible = true),
                style: { "margin-top": "20px" }
              }, {
                default: withCtx(() => _cache[10] || (_cache[10] = [
                  createTextVNode("充值会员")
                ])),
                _: 1
              }),
              createVNode(_component_el_dialog, {
                title: "选择会员类型",
                modelValue: $data.vipDialogVisible,
                "onUpdate:modelValue": _cache[3] || (_cache[3] = ($event) => $data.vipDialogVisible = $event),
                width: "300px"
              }, {
                default: withCtx(() => [
                  createBaseVNode("div", _hoisted_8, [
                    createVNode(_component_el_button, {
                      type: "primary",
                      onClick: _cache[1] || (_cache[1] = ($event) => $options.payVip("monthly"))
                    }, {
                      default: withCtx(() => _cache[11] || (_cache[11] = [
                        createTextVNode("月度会员")
                      ])),
                      _: 1
                    }),
                    createVNode(_component_el_button, {
                      type: "warning",
                      onClick: _cache[2] || (_cache[2] = ($event) => $options.payVip("yearly")),
                      style: { "margin-left": "20px" }
                    }, {
                      default: withCtx(() => _cache[12] || (_cache[12] = [
                        createTextVNode("年度会员")
                      ])),
                      _: 1
                    })
                  ])
                ]),
                _: 1
              }, 8, ["modelValue"]),
              createVNode(_component_el_dialog, {
                title: "修改密码",
                modelValue: $data.dialogVisible,
                "onUpdate:modelValue": _cache[8] || (_cache[8] = ($event) => $data.dialogVisible = $event),
                width: "30%",
                modal: true,
                "lock-scroll": true
              }, {
                footer: withCtx(() => [
                  createVNode(_component_el_button, {
                    onClick: _cache[7] || (_cache[7] = ($event) => $data.dialogVisible = false),
                    style: { "margin-left": "80px" }
                  }, {
                    default: withCtx(() => _cache[13] || (_cache[13] = [
                      createTextVNode("取消")
                    ])),
                    _: 1
                  }),
                  createVNode(_component_el_button, {
                    type: "primary",
                    onClick: $options.handleSave
                  }, {
                    default: withCtx(() => _cache[14] || (_cache[14] = [
                      createTextVNode("保存")
                    ])),
                    _: 1
                  }, 8, ["onClick"])
                ]),
                default: withCtx(() => [
                  createBaseVNode("div", null, [
                    createVNode(_component_el_form, {
                      ref: "editForm",
                      model: $data.editForm,
                      rules: $data.rules,
                      "label-width": "80px"
                    }, {
                      default: withCtx(() => [
                        createVNode(_component_el_form_item, {
                          label: "旧密码",
                          prop: "oldpassword"
                        }, {
                          default: withCtx(() => [
                            createVNode(_component_el_input, {
                              modelValue: $data.editForm.oldpassword,
                              "onUpdate:modelValue": _cache[4] || (_cache[4] = ($event) => $data.editForm.oldpassword = $event),
                              type: "password"
                            }, null, 8, ["modelValue"])
                          ]),
                          _: 1
                        }),
                        createVNode(_component_el_form_item, {
                          label: "密码",
                          prop: "password"
                        }, {
                          default: withCtx(() => [
                            createVNode(_component_el_input, {
                              modelValue: $data.editForm.password,
                              "onUpdate:modelValue": _cache[5] || (_cache[5] = ($event) => $data.editForm.password = $event),
                              type: "password"
                            }, null, 8, ["modelValue"])
                          ]),
                          _: 1
                        }),
                        createVNode(_component_el_form_item, {
                          label: "确认密码",
                          prop: "againpassword"
                        }, {
                          default: withCtx(() => [
                            createVNode(_component_el_input, {
                              modelValue: $data.editForm.againpassword,
                              "onUpdate:modelValue": _cache[6] || (_cache[6] = ($event) => $data.editForm.againpassword = $event),
                              type: "password"
                            }, null, 8, ["modelValue"])
                          ]),
                          _: 1
                        })
                      ]),
                      _: 1
                    }, 8, ["model", "rules"])
                  ])
                ]),
                _: 1
              }, 8, ["modelValue"]),
              createVNode(_component_el_button, {
                type: "primary",
                onClick: $options.openDialog,
                style: { "margin-top": "20px", "margin-left": "10px" }
              }, {
                default: withCtx(() => _cache[15] || (_cache[15] = [
                  createTextVNode("修改密码")
                ])),
                _: 1
              }, 8, ["onClick"]),
              createVNode(_component_el_button, {
                type: "danger",
                onClick: $options.returnhome,
                style: { "margin-top": "20px" }
              }, {
                default: withCtx(() => _cache[16] || (_cache[16] = [
                  createTextVNode("退出登录")
                ])),
                _: 1
              }, 8, ["onClick"])
            ])
          ]),
          _: 1
        })
      ])
    ])
  ]);
}
const User = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render]]);
export {
  User as default
};
