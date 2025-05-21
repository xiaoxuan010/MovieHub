import { _ as _export_sfc, N as Navbar, c as createElementBlock, a as createVNode, r as resolveComponent, b as createBaseVNode, w as withCtx, E as ElMessage, e as axios, f as api, o as openBlock, d as withKeys, g as createTextVNode } from "./index-BHtkpZle.js";
const _sfc_main = {
  name: "Resetpassword",
  components: {
    Navbar
  },
  data() {
    return {
      validForm: {
        email: ""
      },
      resetForm: {
        password: "",
        confirmPassword: ""
      },
      username: "",
      token: "",
      rules: {
        email: [
          {
            required: true,
            message: "请输入原账号邮箱",
            trigger: "blur"
          },
          {
            type: "email",
            message: "请输入正确的邮箱",
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            message: "请输入新密码",
            trigger: "blur"
          },
          {
            min: 5,
            max: 20,
            message: "密码长度在 5 到 20 个字符之间",
            trigger: "blur"
          }
        ],
        confirmPassword: [
          {
            required: true,
            message: "请确认新密码",
            trigger: "blur"
          }
        ]
      }
    };
  },
  mounted() {
  },
  methods: {
    onSubmit() {
      this.$refs.validForm.validate((valid) => {
        if (valid) {
          axios.post(api.apivalidemail, this.validForm).then((response) => {
            if (response.data.code === 200) {
              this.isValid = false;
              ElMessage({
                message: "验证成功，请到邮箱查看重置密码链接",
                type: "success",
                duration: 5e3
              });
              if (this.validForm.email.endsWith("@qq.com")) {
                window.open(
                  "https://mail.qq.com/",
                  "_blank"
                );
              } else if (this.validForm.email.endsWith("@163.com")) {
                window.open(
                  "https://mail.163.com/",
                  "_blank"
                );
              } else if (this.validForm.email.endsWith("@sina.com")) {
                window.open(
                  "https://mail.sina.com.cn/",
                  "_blank"
                );
              } else if (this.validForm.email.endsWith("@gmail.com")) {
                window.open(
                  "https://mail.google.com/",
                  "_blank"
                );
              } else if (this.validForm.email.endsWith("@yahoo.com")) {
                window.open(
                  "https://login.yahoo.com/",
                  "_blank"
                );
              } else if (this.validForm.email.endsWith(
                "@hotmail.com"
              )) {
                window.open(
                  "https://outlook.live.com/",
                  "_blank"
                );
              } else if (this.validForm.email.endsWith(
                "@outlook.com"
              )) {
                window.open(
                  "https://outlook.live.com/",
                  "_blank"
                );
              } else {
                this.$router.push("/");
              }
            } else {
              this.$message.error(response.data.msg);
            }
          });
        } else {
          return false;
        }
      });
    },
    adjustPosition() {
      if (this.token === void 0) {
        const validContent = this.$refs.validContent;
        if (!validContent) return;
        const windowWidth = window.innerWidth;
        const windowHeight = window.innerHeight;
        const contentWidth = validContent.offsetWidth;
        const contentHeight = validContent.offsetHeight;
        const left = (windowWidth - contentWidth) / 2;
        const top = (windowHeight - contentHeight) / 2 - 80;
        validContent.style.left = `${left}px`;
        validContent.style.top = `${top}px`;
      } else {
        const resetContent = this.$refs.resetContent;
        if (!resetContent) return;
        const windowWidth = window.innerWidth;
        const windowHeight = window.innerHeight;
        const contentWidth = resetContent.offsetWidth;
        const contentHeight = resetContent.offsetHeight;
        const left = (windowWidth - contentWidth) / 2;
        const top = (windowHeight - contentHeight) / 2 - 80;
        resetContent.style.left = `${left}px`;
        resetContent.style.top = `${top}px`;
      }
    },
    resetPassword() {
      this.$refs.resetForm.validate((valid) => {
        if (valid) {
          if (this.resetForm.password !== this.resetForm.confirmPassword) {
            ElMessage({
              message: "两次输入的密码不一致",
              type: "error"
            });
            this.resetForm.password = "";
            this.resetForm.confirmPassword = "";
            return false;
          }
          axios.post(api.apiresetpassword, {
            username: this.username,
            token: this.token,
            newPassword: this.resetForm.password
          }).then((response) => {
            if (response.data.code === 200) {
              ElMessage({
                message: "密码重置成功，请登录",
                type: "success"
              });
              this.token = "";
              this.username = "";
              this.resetForm.password = "";
              this.$router.push("/login");
            } else {
              ElMessage({
                message: response.data.message,
                type: "error"
              });
              this.resetForm.password = "";
              this.resetForm.confirmPassword = "";
            }
          });
        } else {
          return false;
        }
      });
    }
  },
  mounted() {
    const { username, token } = this.$route.query;
    this.username = username;
    this.token = token;
    this.$nextTick(() => {
      this.adjustPosition();
    });
    window.addEventListener("resize", this.adjustPosition);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.adjustPosition);
  }
};
const _hoisted_1 = { class: "resetmaindemo" };
const _hoisted_2 = {
  key: 0,
  class: "validcontent",
  ref: "validContent"
};
const _hoisted_3 = { class: "validform" };
const _hoisted_4 = {
  key: 1,
  class: "resetcontent",
  ref: "resetContent"
};
const _hoisted_5 = { class: "resetform" };
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_Navbar = resolveComponent("Navbar");
  const _component_el_input = resolveComponent("el-input");
  const _component_el_form_item = resolveComponent("el-form-item");
  const _component_el_button = resolveComponent("el-button");
  const _component_el_form = resolveComponent("el-form");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createVNode(_component_Navbar),
    !$data.token ? (openBlock(), createElementBlock("div", _hoisted_2, [
      _cache[6] || (_cache[6] = createBaseVNode("p", { class: "validmaintext" }, "找回密码", -1)),
      createBaseVNode("div", _hoisted_3, [
        createVNode(_component_el_form, {
          ref: "validForm",
          model: $data.validForm,
          rules: $data.rules,
          "label-width": "80px",
          class: "demo-ruleForm"
        }, {
          default: withCtx(() => [
            createVNode(_component_el_form_item, {
              label: "原号邮箱",
              prop: "email"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_input, {
                  modelValue: $data.validForm.email,
                  "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.validForm.email = $event)
                }, null, 8, ["modelValue"])
              ]),
              _: 1
            }),
            createVNode(_component_el_form_item, null, {
              default: withCtx(() => [
                createVNode(_component_el_button, {
                  type: "primary",
                  onClick: $options.onSubmit,
                  onKeyup: withKeys($options.onSubmit, ["enter"])
                }, {
                  default: withCtx(() => _cache[4] || (_cache[4] = [
                    createTextVNode("发送")
                  ])),
                  _: 1
                }, 8, ["onClick", "onKeyup"]),
                createVNode(_component_el_button, {
                  type: "info",
                  onClick: _cache[1] || (_cache[1] = ($event) => _ctx.$router.push("/login"))
                }, {
                  default: withCtx(() => _cache[5] || (_cache[5] = [
                    createTextVNode("返回")
                  ])),
                  _: 1
                })
              ]),
              _: 1
            })
          ]),
          _: 1
        }, 8, ["model", "rules"])
      ])
    ], 512)) : (openBlock(), createElementBlock("div", _hoisted_4, [
      _cache[8] || (_cache[8] = createBaseVNode("p", { class: "resetmaintext" }, "找回密码", -1)),
      createBaseVNode("div", _hoisted_5, [
        createVNode(_component_el_form, {
          ref: "resetForm",
          model: $data.resetForm,
          rules: $data.rules,
          "label-width": "80px",
          class: "demo-ruleForm"
        }, {
          default: withCtx(() => [
            createVNode(_component_el_form_item, {
              label: "新密码",
              prop: "password"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_input, {
                  modelValue: $data.resetForm.password,
                  "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => $data.resetForm.password = $event)
                }, null, 8, ["modelValue"])
              ]),
              _: 1
            }),
            createVNode(_component_el_form_item, {
              label: "确认密码",
              prop: "confirmPassword"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_input, {
                  modelValue: $data.resetForm.confirmPassword,
                  "onUpdate:modelValue": _cache[3] || (_cache[3] = ($event) => $data.resetForm.confirmPassword = $event)
                }, null, 8, ["modelValue"])
              ]),
              _: 1
            }),
            createVNode(_component_el_form_item, null, {
              default: withCtx(() => [
                createVNode(_component_el_button, {
                  type: "primary",
                  onClick: $options.resetPassword,
                  onKeyup: withKeys($options.onSubmit, ["enter"])
                }, {
                  default: withCtx(() => _cache[7] || (_cache[7] = [
                    createTextVNode("重置密码")
                  ])),
                  _: 1
                }, 8, ["onClick", "onKeyup"])
              ]),
              _: 1
            })
          ]),
          _: 1
        }, 8, ["model", "rules"])
      ])
    ], 512))
  ]);
}
const Resetpassword = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render]]);
export {
  Resetpassword as default
};
