import { _ as _export_sfc, N as Navbar, c as createElementBlock, a as createVNode, b as createBaseVNode, r as resolveComponent, w as withCtx, d as withKeys, e as axios, f as api, E as ElMessage, o as openBlock, g as createTextVNode } from "./index-BHtkpZle.js";
const _sfc_main = {
  name: "Login",
  components: {
    Navbar
  },
  data() {
    return {
      loginForm: {
        username: "",
        password: ""
      },
      rules: {
        username: [
          {
            required: true,
            message: "请输入用户名",
            trigger: "change"
          },
          {
            min: 3,
            max: 10,
            message: "长度在 3 到 10 个字符之间",
            trigger: "change"
          }
        ],
        password: [
          {
            required: true,
            message: "请输入密码",
            trigger: "change"
          },
          {
            min: 5,
            max: 16,
            message: "长度在 5 到 50 个字符之间",
            trigger: "change"
          }
        ]
      }
    };
  },
  methods: {
    onSubmit() {
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          axios.post(api.loginapi, {
            username: this.loginForm.username,
            password: this.loginForm.password
          }).then((response) => {
            if (response.data.code === 200) {
              localStorage.setItem(
                "token",
                response.data.data.token
              );
              localStorage.setItem(
                "username",
                this.loginForm.username
              );
              ElMessage({
                message: "登录成功！",
                type: "success"
              });
              this.$router.push("/");
            } else {
              this.loginForm.password = "";
              ElMessage({
                message: response.data.message,
                type: "error"
              });
              return false;
            }
          }).catch((error) => {
            console.error("登录请求失败:", error);
            ElMessage({
              message: "登录请求失败，请稍后再试",
              type: "error"
            });
          });
        } else {
          ElMessage({
            message: "登录失败，请检查用户名和密码",
            type: "error"
          });
          return false;
        }
      });
    },
    register() {
      this.$router.push("/register");
    },
    adjustPosition() {
      const loginContent = this.$refs.loginContent;
      const windowWidth = window.innerWidth;
      const windowHeight = window.innerHeight;
      const contentWidth = loginContent.offsetWidth;
      const contentHeight = loginContent.offsetHeight;
      const left = (windowWidth - contentWidth) / 2;
      const top = (windowHeight - contentHeight) / 2 - 80;
      loginContent.style.left = `${left}px`;
      loginContent.style.top = `${top}px`;
    },
    findPassword() {
      this.$router.push("/reset-password");
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.adjustPosition();
    });
    window.addEventListener("resize", this.adjustPosition);
  },
  beforeDestroy() {
    window.removeEventListener("resize", this.adjustPosition);
  }
};
const _hoisted_1 = { class: "loginmaindemo" };
const _hoisted_2 = {
  class: "logincontent",
  ref: "loginContent"
};
const _hoisted_3 = { class: "loginform" };
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_Navbar = resolveComponent("Navbar");
  const _component_el_input = resolveComponent("el-input");
  const _component_el_form_item = resolveComponent("el-form-item");
  const _component_el_button = resolveComponent("el-button");
  const _component_el_form = resolveComponent("el-form");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createVNode(_component_Navbar),
    createBaseVNode("div", _hoisted_2, [
      _cache[5] || (_cache[5] = createBaseVNode("p", { class: "loginmaintext" }, "登录", -1)),
      createBaseVNode("div", _hoisted_3, [
        createVNode(_component_el_form, {
          ref: "loginForm",
          model: $data.loginForm,
          rules: $data.rules,
          "label-width": "80px",
          class: "demo-ruleForm",
          onKeyup: withKeys($options.onSubmit, ["enter", "native"])
        }, {
          default: withCtx(() => [
            createVNode(_component_el_form_item, {
              label: "用户名",
              prop: "username"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_input, {
                  modelValue: $data.loginForm.username,
                  "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.loginForm.username = $event)
                }, null, 8, ["modelValue"])
              ]),
              _: 1
            }),
            createVNode(_component_el_form_item, {
              label: "密码",
              prop: "password",
              style: { "margin-bottom": "5px" }
            }, {
              default: withCtx(() => [
                createVNode(_component_el_input, {
                  type: "password",
                  modelValue: $data.loginForm.password,
                  "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => $data.loginForm.password = $event)
                }, null, 8, ["modelValue"])
              ]),
              _: 1
            }),
            createVNode(_component_el_form_item, { style: { "margin": "0" } }, {
              default: withCtx(() => [
                createVNode(_component_el_button, {
                  type: "text",
                  class: "captcha-btn",
                  onClick: $options.findPassword
                }, {
                  default: withCtx(() => _cache[2] || (_cache[2] = [
                    createTextVNode("忘记密码？")
                  ])),
                  _: 1
                }, 8, ["onClick"])
              ]),
              _: 1
            }),
            createVNode(_component_el_form_item, { style: { "margin": "0" } }, {
              default: withCtx(() => [
                createVNode(_component_el_button, {
                  type: "primary",
                  onClick: $options.onSubmit
                }, {
                  default: withCtx(() => _cache[3] || (_cache[3] = [
                    createTextVNode("登录")
                  ])),
                  _: 1
                }, 8, ["onClick"]),
                createVNode(_component_el_button, {
                  type: "info",
                  onClick: $options.register
                }, {
                  default: withCtx(() => _cache[4] || (_cache[4] = [
                    createTextVNode("注册")
                  ])),
                  _: 1
                }, 8, ["onClick"])
              ]),
              _: 1
            })
          ]),
          _: 1
        }, 8, ["model", "rules", "onKeyup"])
      ])
    ], 512)
  ]);
}
const Login = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render]]);
export {
  Login as default
};
