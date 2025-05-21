import { _ as _export_sfc, c as createElementBlock, b as createBaseVNode, a as createVNode, w as withCtx, r as resolveComponent, E as ElMessage, e as axios, f as api, o as openBlock, d as withKeys, g as createTextVNode } from "./index-Zb4gIbIS.js";
const _sfc_main = {
  name: "Adlogin",
  data() {
    return {
      form: {
        username: "",
        password: ""
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" }
        ]
      }
    };
  },
  mounted() {
    if (localStorage.getItem("username") === "admin") {
      this.$router.push({ path: "/admin/Adhome" });
    }
  },
  methods: {
    onSubmit() {
      if (this.form.username != "admin") {
        ElMessage.error("该用户不是管理员");
        return;
      }
      this.$refs.loginForm.validate((valid) => {
        if (valid) {
          axios.post(api.loginapi, {
            username: this.form.username,
            password: this.form.password
          }).then((response) => {
            if (response.data.code === 200) {
              localStorage.setItem(
                "token",
                response.data.data.token
              );
              localStorage.setItem(
                "username",
                this.form.username
              );
              ElMessage.success("登录成功");
              this.$router.push({ path: "/admin/Adhome" });
            } else {
              ElMessage.error("用户名或密码错误");
              this.form.password = "";
              this.form.username = "";
            }
          }).catch(() => {
            ElMessage.error("登录请求失败");
          });
        }
      });
    }
  }
};
const _hoisted_1 = { class: "Adlogin" };
const _hoisted_2 = { class: "login-container" };
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_el_input = resolveComponent("el-input");
  const _component_el_form_item = resolveComponent("el-form-item");
  const _component_el_button = resolveComponent("el-button");
  const _component_el_form = resolveComponent("el-form");
  const _component_el_card = resolveComponent("el-card");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createBaseVNode("div", _hoisted_2, [
      createVNode(_component_el_card, { class: "login-card" }, {
        default: withCtx(() => [
          _cache[3] || (_cache[3] = createBaseVNode("h2", { class: "login-title" }, "管理员登录", -1)),
          createVNode(_component_el_form, {
            model: $data.form,
            rules: $data.rules,
            ref: "loginForm",
            "label-width": "80px",
            class: "Adlogin-form",
            onKeyup: withKeys($options.onSubmit, ["enter", "native"])
          }, {
            default: withCtx(() => [
              createVNode(_component_el_form_item, {
                label: "用户名",
                prop: "username"
              }, {
                default: withCtx(() => [
                  createVNode(_component_el_input, {
                    modelValue: $data.form.username,
                    "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.form.username = $event),
                    placeholder: "请输入用户名"
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
                    modelValue: $data.form.password,
                    "onUpdate:modelValue": _cache[1] || (_cache[1] = ($event) => $data.form.password = $event),
                    type: "password",
                    placeholder: "请输入密码",
                    "show-password": ""
                  }, null, 8, ["modelValue"])
                ]),
                _: 1
              }),
              createVNode(_component_el_form_item, null, {
                default: withCtx(() => [
                  createVNode(_component_el_button, {
                    type: "primary",
                    onClick: $options.onSubmit,
                    style: { "width": "90%" }
                  }, {
                    default: withCtx(() => _cache[2] || (_cache[2] = [
                      createTextVNode("登录")
                    ])),
                    _: 1
                  }, 8, ["onClick"])
                ]),
                _: 1
              })
            ]),
            _: 1
          }, 8, ["model", "rules", "onKeyup"])
        ]),
        _: 1
      })
    ])
  ]);
}
const Adlogin = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render]]);
export {
  Adlogin as default
};
