import { _ as _export_sfc, N as Navbar, c as createElementBlock, a as createVNode, b as createBaseVNode, r as resolveComponent, o as openBlock } from "./index-Zb4gIbIS.js";
const _sfc_main = {
  name: "Movies",
  components: {
    Navbar
  },
  data() {
    return {};
  },
  mounted() {
  },
  methods: {}
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_Navbar = resolveComponent("Navbar");
  return openBlock(), createElementBlock("div", null, [
    createVNode(_component_Navbar),
    _cache[0] || (_cache[0] = createBaseVNode("div", { class: "moviecontent" }, [
      createBaseVNode("div", { class: "moviemaindemo" })
    ], -1))
  ]);
}
const Movies = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render]]);
export {
  Movies as default
};
