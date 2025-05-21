import { _ as _export_sfc, N as Navbar, c as createElementBlock, a as createVNode, b as createBaseVNode, r as resolveComponent, w as withCtx, o as openBlock, g as createTextVNode } from "./index-BHtkpZle.js";
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
const _hoisted_4 = { style: { "display": "flex", "justify-content": "center", "margin-top": "30px" } };
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_Navbar = resolveComponent("Navbar");
  const _component_el_descriptions_item = resolveComponent("el-descriptions-item");
  const _component_el_tag = resolveComponent("el-tag");
  const _component_el_descriptions = resolveComponent("el-descriptions");
  const _component_el_button = resolveComponent("el-button");
  const _component_el_card = resolveComponent("el-card");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createVNode(_component_Navbar),
    createBaseVNode("div", _hoisted_2, [
      createVNode(_component_el_card, { class: "aboutcard" }, {
        header: withCtx(() => _cache[0] || (_cache[0] = [
          createBaseVNode("div", { class: "abouttoptext" }, "后台管理", -1)
        ])),
        default: withCtx(() => [
          createBaseVNode("div", _hoisted_3, [
            createVNode(_component_el_descriptions, {
              title: "管理员信息",
              class: "descriptionsinfo",
              column: 3,
              "label-width": "80px"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_descriptions_item, { label: "账号" }, {
                  default: withCtx(() => _cache[1] || (_cache[1] = [
                    createTextVNode("admin")
                  ])),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item, { label: "密码" }, {
                  default: withCtx(() => _cache[2] || (_cache[2] = [
                    createTextVNode("admin")
                  ])),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item, { label: "权限" }, {
                  default: withCtx(() => _cache[3] || (_cache[3] = [
                    createTextVNode("管理员")
                  ])),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item, { label: "状态" }, {
                  default: withCtx(() => [
                    createVNode(_component_el_tag, {
                      size: "small",
                      type: "success",
                      effect: "dark",
                      round: ""
                    }, {
                      default: withCtx(() => _cache[4] || (_cache[4] = [
                        createTextVNode("正常")
                      ])),
                      _: 1
                    })
                  ]),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item, { label: "邮箱" }, {
                  default: withCtx(() => _cache[5] || (_cache[5] = [
                    createTextVNode("admin@astralbridge.space")
                  ])),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item)
              ]),
              _: 1
            }),
            createVNode(_component_el_descriptions, {
              title: "初始用户1",
              class: "descriptionsinfo",
              column: 3,
              "label-width": "80px"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_descriptions_item, { label: "账号" }, {
                  default: withCtx(() => _cache[6] || (_cache[6] = [
                    createTextVNode("user1")
                  ])),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item, { label: "密码" }, {
                  default: withCtx(() => _cache[7] || (_cache[7] = [
                    createTextVNode("user1")
                  ])),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item, { label: "权限" }, {
                  default: withCtx(() => _cache[8] || (_cache[8] = [
                    createTextVNode("普通用户")
                  ])),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item, { label: "状态" }, {
                  default: withCtx(() => [
                    createVNode(_component_el_tag, {
                      size: "small",
                      type: "success",
                      effect: "dark",
                      round: ""
                    }, {
                      default: withCtx(() => _cache[9] || (_cache[9] = [
                        createTextVNode("正常")
                      ])),
                      _: 1
                    })
                  ]),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item, { label: "邮箱" }, {
                  default: withCtx(() => _cache[10] || (_cache[10] = [
                    createTextVNode("xiaoxuan010n@qq.com")
                  ])),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item)
              ]),
              _: 1
            }),
            createVNode(_component_el_descriptions, {
              title: "初始用户2",
              class: "descriptionsinfo",
              column: 3,
              "label-width": "80px"
            }, {
              default: withCtx(() => [
                createVNode(_component_el_descriptions_item, { label: "账号" }, {
                  default: withCtx(() => _cache[11] || (_cache[11] = [
                    createTextVNode("vipuser")
                  ])),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item, { label: "密码" }, {
                  default: withCtx(() => _cache[12] || (_cache[12] = [
                    createTextVNode("vipuser")
                  ])),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item, { label: "权限" }, {
                  default: withCtx(() => _cache[13] || (_cache[13] = [
                    createTextVNode("Vip用户")
                  ])),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item, { label: "状态" }, {
                  default: withCtx(() => [
                    createVNode(_component_el_tag, {
                      size: "small",
                      type: "success",
                      effect: "dark",
                      round: ""
                    }, {
                      default: withCtx(() => _cache[14] || (_cache[14] = [
                        createTextVNode("正常")
                      ])),
                      _: 1
                    })
                  ]),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item, { label: "邮箱" }, {
                  default: withCtx(() => _cache[15] || (_cache[15] = [
                    createTextVNode("vip@astralbridge.space")
                  ])),
                  _: 1
                }),
                createVNode(_component_el_descriptions_item)
              ]),
              _: 1
            }),
            createBaseVNode("div", _hoisted_4, [
              createVNode(_component_el_button, {
                type: "primary",
                onClick: $options.toadminpage,
                round: ""
              }, {
                default: withCtx(() => _cache[16] || (_cache[16] = [
                  createTextVNode(" 前往管理后台 ")
                ])),
                _: 1
              }, 8, ["onClick"])
            ])
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
