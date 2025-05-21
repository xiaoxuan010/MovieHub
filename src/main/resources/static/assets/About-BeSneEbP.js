import { _ as _export_sfc, N as Navbar, c as createElementBlock, a as createVNode, b as createBaseVNode, r as resolveComponent, w as withCtx, o as openBlock, g as createTextVNode } from "./index-Zb4gIbIS.js";
const _sfc_main = {
  name: "About",
  components: {
    Navbar
  },
  data() {
    return {};
  },
  mounted() {
  },
  methods: {
    toadminpage() {
      if (localStorage.getItem("username") != "admin" || !localStorage.getItem("token")) {
        this.$router.push({ path: "/admin" });
        return;
      }
      this.$router.push({ path: "/admin/Adhome" });
    }
  }
};
const _hoisted_1 = { class: "aboutmaindemo" };
const _hoisted_2 = { class: "aboutmaindemo" };
const _hoisted_3 = { style: { "margin-left": "20px" } };
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_Navbar = resolveComponent("Navbar");
  const _component_el_button = resolveComponent("el-button");
  const _component_el_card = resolveComponent("el-card");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createVNode(_component_Navbar),
    createBaseVNode("div", _hoisted_2, [
      createVNode(_component_el_card, { class: "aboutcard" }, {
        header: withCtx(() => _cache[0] || (_cache[0] = [
          createBaseVNode("div", { class: "abouttoptext" }, "关于我们", -1)
        ])),
        default: withCtx(() => [
          createBaseVNode("div", _hoisted_3, [
            createVNode(_component_el_button, {
              type: "primary",
              onClick: $options.toadminpage
            }, {
              default: withCtx(() => _cache[1] || (_cache[1] = [
                createTextVNode(" 前往管理后台")
              ])),
              _: 1
            }, 8, ["onClick"])
          ])
        ]),
        _: 1
      })
    ])
  ]);
}
const About = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render]]);
export {
  About as default
};
