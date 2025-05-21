import { _ as _export_sfc, N as Navbar, c as createElementBlock, a as createVNode, b as createBaseVNode, r as resolveComponent, w as withCtx, d as withKeys, e as axios, f as api, E as ElMessage, o as openBlock, g as createTextVNode } from "./index-Zb4gIbIS.js";
const _sfc_main = {
  name: "Register",
  components: {
    Navbar
  },
  data() {
    return {
      registerForm: {
        username: "",
        password: "",
        email: ""
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
            min: 6,
            max: 16,
            message: "长度在 6 到 50 个字符之间",
            trigger: "change"
          }
        ],
        email: [
          {
            required: true,
            message: "请输入正确的邮箱",
            trigger: "change"
          },
          {
            min: 3,
            max: 50,
            message: "长度在 3 到 50 个字符之间",
            trigger: "change"
          }
        ]
      }
    };
  },
  methods: {
    onSubmit() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          axios.post(api.registerapi, this.registerForm).then((response) => {
            if (response.data.code === 200) {
              ElMessage({
                message: "注册成功！",
                type: "success"
              });
              this.$router.push("/login");
            } else {
              if (response.data.message === "用户名已存在") {
                ElMessage({
                  message: response.data.message,
                  type: "error"
                });
                this.registerForm = {
                  username: "",
                  password: "",
                  email: ""
                };
                return false;
              }
              if (response.data.data.email === "不是一个合法的电子邮件地址") {
                ElMessage({
                  message: response.data.data.email,
                  type: "error"
                });
                this.registerForm.email = "";
                return false;
              } else {
                ElMessage({
                  message: response.data.message,
                  type: "error"
                });
                return false;
              }
            }
          }).catch((error) => {
            console.error("注册请求失败:", error);
            return false;
          });
        } else {
          ElMessage({
            message: "请填写正确的信息",
            type: "error"
          });
          return false;
        }
      });
    },
    adjustPosition() {
      const registerContent = this.$refs.registerContent;
      const windowWidth = window.innerWidth;
      const windowHeight = window.innerHeight;
      const contentWidth = registerContent.offsetWidth;
      const contentHeight = registerContent.offsetHeight;
      const left = (windowWidth - contentWidth) / 2;
      const top = (windowHeight - contentHeight) / 2 - 80;
      registerContent.style.left = `${left}px`;
      registerContent.style.top = `${top}px`;
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
const _hoisted_1 = { class: "registermaindemo" };
const _hoisted_2 = {
  class: "registercontent",
  ref: "registerContent"
};
const _hoisted_3 = { class: "registerform" };
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_Navbar = resolveComponent("Navbar");
  const _component_el_input = resolveComponent("el-input");
  const _component_el_form_item = resolveComponent("el-form-item");
  const _component_el_button = resolveComponent("el-button");
  const _component_el_form = resolveComponent("el-form");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createVNode(_component_Navbar),
    createBaseVNode("div", _hoisted_2, [
      _cache[6] || (_cache[6] = createBaseVNode("p", { class: "registertext" }, "注册", -1)),
      createBaseVNode("div", _hoisted_3, [
        createVNode(_component_el_form, {
          ref: "registerForm",
          model: $data.registerForm,
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
                  modelValue: $data.registerForm.username,
                  "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.registerForm.username = $event)
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
                  type: "password",
                  modelValue: $data.registerForm.password,
                  "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => $data.registerForm.password = $event)
                }, null, 8, ["modelValue"])
              ]),
              _: 1
            }),
            createVNode(_component_el_form_item, {
              label: "邮箱",
              prop: "email"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_input, {
                  type: "email",
                  modelValue: $data.registerForm.email,
                  "onUpdate:modelValue": _cache[2] || (_cache[2] = ($event) => $data.registerForm.email = $event)
                }, null, 8, ["modelValue"])
              ]),
              _: 1
            }),
            createVNode(_component_el_form_item, null, {
              default: withCtx(() => [
                createVNode(_component_el_button, {
                  type: "primary",
                  onClick: $options.onSubmit
                }, {
                  default: withCtx(() => _cache[4] || (_cache[4] = [
                    createTextVNode("注册")
                  ])),
                  _: 1
                }, 8, ["onClick"]),
                createVNode(_component_el_button, {
                  type: "success",
                  onClick: _cache[3] || (_cache[3] = ($event) => _ctx.$router.push("/login"))
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
        }, 8, ["model", "rules", "onKeyup"])
      ])
    ], 512)
  ]);
}
const Register = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render]]);
export {
  Register as default
};
