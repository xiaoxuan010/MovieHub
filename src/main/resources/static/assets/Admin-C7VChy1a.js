import { a as createVNode, I as Icon, _ as _export_sfc, j as createBlock, w as withCtx, r as resolveComponent, o as openBlock, b as createBaseVNode, m as defineComponent, c as createElementBlock, n as normalizeStyle } from "./index-Zb4gIbIS.js";
var CodeSandboxOutlined$1 = { "icon": { "tag": "svg", "attrs": { "viewBox": "64 64 896 896", "focusable": "false" }, "children": [{ "tag": "path", "attrs": { "d": "M709.6 210l.4-.2h.2L512 96 313.9 209.8h-.2l.7.3L151.5 304v416L512 928l360.5-208V304l-162.9-94zM482.7 843.6L339.6 761V621.4L210 547.8V372.9l272.7 157.3v313.4zM238.2 321.5l134.7-77.8 138.9 79.7 139.1-79.9 135.2 78-273.9 158-274-158zM814 548.3l-128.8 73.1v139.1l-143.9 83V530.4L814 373.1v175.2z" } }] }, "name": "code-sandbox", "theme": "outlined" };
function _objectSpread$6(target) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i] != null ? Object(arguments[i]) : {};
    var ownKeys = Object.keys(source);
    if (typeof Object.getOwnPropertySymbols === "function") {
      ownKeys = ownKeys.concat(Object.getOwnPropertySymbols(source).filter(function(sym) {
        return Object.getOwnPropertyDescriptor(source, sym).enumerable;
      }));
    }
    ownKeys.forEach(function(key) {
      _defineProperty$6(target, key, source[key]);
    });
  }
  return target;
}
function _defineProperty$6(obj, key, value) {
  if (key in obj) {
    Object.defineProperty(obj, key, { value, enumerable: true, configurable: true, writable: true });
  } else {
    obj[key] = value;
  }
  return obj;
}
var CodeSandboxOutlined = function CodeSandboxOutlined2(props, context) {
  var p = _objectSpread$6({}, props, context.attrs);
  return createVNode(Icon, _objectSpread$6({}, p, {
    "icon": CodeSandboxOutlined$1
  }), null);
};
CodeSandboxOutlined.displayName = "CodeSandboxOutlined";
CodeSandboxOutlined.inheritAttrs = false;
var MenuFoldOutlined$1 = { "icon": { "tag": "svg", "attrs": { "viewBox": "64 64 896 896", "focusable": "false" }, "children": [{ "tag": "path", "attrs": { "d": "M408 442h480c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8H408c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8zm-8 204c0 4.4 3.6 8 8 8h480c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8H408c-4.4 0-8 3.6-8 8v56zm504-486H120c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h784c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8zm0 632H120c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h784c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8zM115.4 518.9L271.7 642c5.8 4.6 14.4.5 14.4-6.9V388.9c0-7.4-8.5-11.5-14.4-6.9L115.4 505.1a8.74 8.74 0 000 13.8z" } }] }, "name": "menu-fold", "theme": "outlined" };
function _objectSpread$5(target) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i] != null ? Object(arguments[i]) : {};
    var ownKeys = Object.keys(source);
    if (typeof Object.getOwnPropertySymbols === "function") {
      ownKeys = ownKeys.concat(Object.getOwnPropertySymbols(source).filter(function(sym) {
        return Object.getOwnPropertyDescriptor(source, sym).enumerable;
      }));
    }
    ownKeys.forEach(function(key) {
      _defineProperty$5(target, key, source[key]);
    });
  }
  return target;
}
function _defineProperty$5(obj, key, value) {
  if (key in obj) {
    Object.defineProperty(obj, key, { value, enumerable: true, configurable: true, writable: true });
  } else {
    obj[key] = value;
  }
  return obj;
}
var MenuFoldOutlined = function MenuFoldOutlined2(props, context) {
  var p = _objectSpread$5({}, props, context.attrs);
  return createVNode(Icon, _objectSpread$5({}, p, {
    "icon": MenuFoldOutlined$1
  }), null);
};
MenuFoldOutlined.displayName = "MenuFoldOutlined";
MenuFoldOutlined.inheritAttrs = false;
var MenuUnfoldOutlined$1 = { "icon": { "tag": "svg", "attrs": { "viewBox": "64 64 896 896", "focusable": "false" }, "children": [{ "tag": "path", "attrs": { "d": "M408 442h480c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8H408c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8zm-8 204c0 4.4 3.6 8 8 8h480c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8H408c-4.4 0-8 3.6-8 8v56zm504-486H120c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h784c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8zm0 632H120c-4.4 0-8 3.6-8 8v56c0 4.4 3.6 8 8 8h784c4.4 0 8-3.6 8-8v-56c0-4.4-3.6-8-8-8zM142.4 642.1L298.7 519a8.84 8.84 0 000-13.9L142.4 381.9c-5.8-4.6-14.4-.5-14.4 6.9v246.3a8.9 8.9 0 0014.4 7z" } }] }, "name": "menu-unfold", "theme": "outlined" };
function _objectSpread$4(target) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i] != null ? Object(arguments[i]) : {};
    var ownKeys = Object.keys(source);
    if (typeof Object.getOwnPropertySymbols === "function") {
      ownKeys = ownKeys.concat(Object.getOwnPropertySymbols(source).filter(function(sym) {
        return Object.getOwnPropertyDescriptor(source, sym).enumerable;
      }));
    }
    ownKeys.forEach(function(key) {
      _defineProperty$4(target, key, source[key]);
    });
  }
  return target;
}
function _defineProperty$4(obj, key, value) {
  if (key in obj) {
    Object.defineProperty(obj, key, { value, enumerable: true, configurable: true, writable: true });
  } else {
    obj[key] = value;
  }
  return obj;
}
var MenuUnfoldOutlined = function MenuUnfoldOutlined2(props, context) {
  var p = _objectSpread$4({}, props, context.attrs);
  return createVNode(Icon, _objectSpread$4({}, p, {
    "icon": MenuUnfoldOutlined$1
  }), null);
};
MenuUnfoldOutlined.displayName = "MenuUnfoldOutlined";
MenuUnfoldOutlined.inheritAttrs = false;
var PoweroffOutlined$1 = { "icon": { "tag": "svg", "attrs": { "viewBox": "64 64 896 896", "focusable": "false" }, "children": [{ "tag": "path", "attrs": { "d": "M705.6 124.9a8 8 0 00-11.6 7.2v64.2c0 5.5 2.9 10.6 7.5 13.6a352.2 352.2 0 0162.2 49.8c32.7 32.8 58.4 70.9 76.3 113.3a355 355 0 0127.9 138.7c0 48.1-9.4 94.8-27.9 138.7a355.92 355.92 0 01-76.3 113.3 353.06 353.06 0 01-113.2 76.4c-43.8 18.6-90.5 28-138.5 28s-94.7-9.4-138.5-28a353.06 353.06 0 01-113.2-76.4A355.92 355.92 0 01184 650.4a355 355 0 01-27.9-138.7c0-48.1 9.4-94.8 27.9-138.7 17.9-42.4 43.6-80.5 76.3-113.3 19-19 39.8-35.6 62.2-49.8 4.7-2.9 7.5-8.1 7.5-13.6V132c0-6-6.3-9.8-11.6-7.2C178.5 195.2 82 339.3 80 506.3 77.2 745.1 272.5 943.5 511.2 944c239 .5 432.8-193.3 432.8-432.4 0-169.2-97-315.7-238.4-386.7zM480 560h64c4.4 0 8-3.6 8-8V88c0-4.4-3.6-8-8-8h-64c-4.4 0-8 3.6-8 8v464c0 4.4 3.6 8 8 8z" } }] }, "name": "poweroff", "theme": "outlined" };
function _objectSpread$3(target) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i] != null ? Object(arguments[i]) : {};
    var ownKeys = Object.keys(source);
    if (typeof Object.getOwnPropertySymbols === "function") {
      ownKeys = ownKeys.concat(Object.getOwnPropertySymbols(source).filter(function(sym) {
        return Object.getOwnPropertyDescriptor(source, sym).enumerable;
      }));
    }
    ownKeys.forEach(function(key) {
      _defineProperty$3(target, key, source[key]);
    });
  }
  return target;
}
function _defineProperty$3(obj, key, value) {
  if (key in obj) {
    Object.defineProperty(obj, key, { value, enumerable: true, configurable: true, writable: true });
  } else {
    obj[key] = value;
  }
  return obj;
}
var PoweroffOutlined = function PoweroffOutlined2(props, context) {
  var p = _objectSpread$3({}, props, context.attrs);
  return createVNode(Icon, _objectSpread$3({}, p, {
    "icon": PoweroffOutlined$1
  }), null);
};
PoweroffOutlined.displayName = "PoweroffOutlined";
PoweroffOutlined.inheritAttrs = false;
var TeamOutlined$1 = { "icon": { "tag": "svg", "attrs": { "viewBox": "64 64 896 896", "focusable": "false" }, "children": [{ "tag": "path", "attrs": { "d": "M824.2 699.9a301.55 301.55 0 00-86.4-60.4C783.1 602.8 812 546.8 812 484c0-110.8-92.4-201.7-203.2-200-109.1 1.7-197 90.6-197 200 0 62.8 29 118.8 74.2 155.5a300.95 300.95 0 00-86.4 60.4C345 754.6 314 826.8 312 903.8a8 8 0 008 8.2h56c4.3 0 7.9-3.4 8-7.7 1.9-58 25.4-112.3 66.7-153.5A226.62 226.62 0 01612 684c60.9 0 118.2 23.7 161.3 66.8C814.5 792 838 846.3 840 904.3c.1 4.3 3.7 7.7 8 7.7h56a8 8 0 008-8.2c-2-77-33-149.2-87.8-203.9zM612 612c-34.2 0-66.4-13.3-90.5-37.5a126.86 126.86 0 01-37.5-91.8c.3-32.8 13.4-64.5 36.3-88 24-24.6 56.1-38.3 90.4-38.7 33.9-.3 66.8 12.9 91 36.6 24.8 24.3 38.4 56.8 38.4 91.4 0 34.2-13.3 66.3-37.5 90.5A127.3 127.3 0 01612 612zM361.5 510.4c-.9-8.7-1.4-17.5-1.4-26.4 0-15.9 1.5-31.4 4.3-46.5.7-3.6-1.2-7.3-4.5-8.8-13.6-6.1-26.1-14.5-36.9-25.1a127.54 127.54 0 01-38.7-95.4c.9-32.1 13.8-62.6 36.3-85.6 24.7-25.3 57.9-39.1 93.2-38.7 31.9.3 62.7 12.6 86 34.4 7.9 7.4 14.7 15.6 20.4 24.4 2 3.1 5.9 4.4 9.3 3.2 17.6-6.1 36.2-10.4 55.3-12.4 5.6-.6 8.8-6.6 6.3-11.6-32.5-64.3-98.9-108.7-175.7-109.9-110.9-1.7-203.3 89.2-203.3 199.9 0 62.8 28.9 118.8 74.2 155.5-31.8 14.7-61.1 35-86.5 60.4-54.8 54.7-85.8 126.9-87.8 204a8 8 0 008 8.2h56.1c4.3 0 7.9-3.4 8-7.7 1.9-58 25.4-112.3 66.7-153.5 29.4-29.4 65.4-49.8 104.7-59.7 3.9-1 6.5-4.7 6-8.7z" } }] }, "name": "team", "theme": "outlined" };
function _objectSpread$2(target) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i] != null ? Object(arguments[i]) : {};
    var ownKeys = Object.keys(source);
    if (typeof Object.getOwnPropertySymbols === "function") {
      ownKeys = ownKeys.concat(Object.getOwnPropertySymbols(source).filter(function(sym) {
        return Object.getOwnPropertyDescriptor(source, sym).enumerable;
      }));
    }
    ownKeys.forEach(function(key) {
      _defineProperty$2(target, key, source[key]);
    });
  }
  return target;
}
function _defineProperty$2(obj, key, value) {
  if (key in obj) {
    Object.defineProperty(obj, key, { value, enumerable: true, configurable: true, writable: true });
  } else {
    obj[key] = value;
  }
  return obj;
}
var TeamOutlined = function TeamOutlined2(props, context) {
  var p = _objectSpread$2({}, props, context.attrs);
  return createVNode(Icon, _objectSpread$2({}, p, {
    "icon": TeamOutlined$1
  }), null);
};
TeamOutlined.displayName = "TeamOutlined";
TeamOutlined.inheritAttrs = false;
var UserOutlined$1 = { "icon": { "tag": "svg", "attrs": { "viewBox": "64 64 896 896", "focusable": "false" }, "children": [{ "tag": "path", "attrs": { "d": "M858.5 763.6a374 374 0 00-80.6-119.5 375.63 375.63 0 00-119.5-80.6c-.4-.2-.8-.3-1.2-.5C719.5 518 760 444.7 760 362c0-137-111-248-248-248S264 225 264 362c0 82.7 40.5 156 102.8 201.1-.4.2-.8.3-1.2.5-44.8 18.9-85 46-119.5 80.6a375.63 375.63 0 00-80.6 119.5A371.7 371.7 0 00136 901.8a8 8 0 008 8.2h60c4.4 0 7.9-3.5 8-7.8 2-77.2 33-149.5 87.8-204.3 56.7-56.7 132-87.9 212.2-87.9s155.5 31.2 212.2 87.9C779 752.7 810 825 812 902.2c.1 4.4 3.6 7.8 8 7.8h60a8 8 0 008-8.2c-1-47.8-10.9-94.3-29.5-138.2zM512 534c-45.9 0-89.1-17.9-121.6-50.4S340 407.9 340 362c0-45.9 17.9-89.1 50.4-121.6S466.1 190 512 190s89.1 17.9 121.6 50.4S684 316.1 684 362c0 45.9-17.9 89.1-50.4 121.6S557.9 534 512 534z" } }] }, "name": "user", "theme": "outlined" };
function _objectSpread$1(target) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i] != null ? Object(arguments[i]) : {};
    var ownKeys = Object.keys(source);
    if (typeof Object.getOwnPropertySymbols === "function") {
      ownKeys = ownKeys.concat(Object.getOwnPropertySymbols(source).filter(function(sym) {
        return Object.getOwnPropertyDescriptor(source, sym).enumerable;
      }));
    }
    ownKeys.forEach(function(key) {
      _defineProperty$1(target, key, source[key]);
    });
  }
  return target;
}
function _defineProperty$1(obj, key, value) {
  if (key in obj) {
    Object.defineProperty(obj, key, { value, enumerable: true, configurable: true, writable: true });
  } else {
    obj[key] = value;
  }
  return obj;
}
var UserOutlined = function UserOutlined2(props, context) {
  var p = _objectSpread$1({}, props, context.attrs);
  return createVNode(Icon, _objectSpread$1({}, p, {
    "icon": UserOutlined$1
  }), null);
};
UserOutlined.displayName = "UserOutlined";
UserOutlined.inheritAttrs = false;
var VideoCameraOutlined$1 = { "icon": { "tag": "svg", "attrs": { "viewBox": "64 64 896 896", "focusable": "false" }, "children": [{ "tag": "path", "attrs": { "d": "M912 302.3L784 376V224c0-35.3-28.7-64-64-64H128c-35.3 0-64 28.7-64 64v576c0 35.3 28.7 64 64 64h592c35.3 0 64-28.7 64-64V648l128 73.7c21.3 12.3 48-3.1 48-27.6V330c0-24.6-26.7-40-48-27.7zM712 792H136V232h576v560zm176-167l-104-59.8V458.9L888 399v226zM208 360h112c4.4 0 8-3.6 8-8v-48c0-4.4-3.6-8-8-8H208c-4.4 0-8 3.6-8 8v48c0 4.4 3.6 8 8 8z" } }] }, "name": "video-camera", "theme": "outlined" };
function _objectSpread(target) {
  for (var i = 1; i < arguments.length; i++) {
    var source = arguments[i] != null ? Object(arguments[i]) : {};
    var ownKeys = Object.keys(source);
    if (typeof Object.getOwnPropertySymbols === "function") {
      ownKeys = ownKeys.concat(Object.getOwnPropertySymbols(source).filter(function(sym) {
        return Object.getOwnPropertyDescriptor(source, sym).enumerable;
      }));
    }
    ownKeys.forEach(function(key) {
      _defineProperty(target, key, source[key]);
    });
  }
  return target;
}
function _defineProperty(obj, key, value) {
  if (key in obj) {
    Object.defineProperty(obj, key, { value, enumerable: true, configurable: true, writable: true });
  } else {
    obj[key] = value;
  }
  return obj;
}
var VideoCameraOutlined = function VideoCameraOutlined2(props, context) {
  var p = _objectSpread({}, props, context.attrs);
  return createVNode(Icon, _objectSpread({}, p, {
    "icon": VideoCameraOutlined$1
  }), null);
};
VideoCameraOutlined.displayName = "VideoCameraOutlined";
VideoCameraOutlined.inheritAttrs = false;
const _sfc_main$2 = {
  name: "Aside",
  components: {
    UserOutlined,
    VideoCameraOutlined,
    CodeSandboxOutlined,
    TeamOutlined
  },
  props: {
    collapsed: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      selectedKeys: ["1"]
    };
  },
  watch: {
    // 路由变化时同步高亮
    $route(to) {
      this.updateSelectedKeys(to.path);
    }
  },
  mounted() {
    this.updateSelectedKeys(this.$route.path);
  },
  methods: {
    updateSelectedKeys(path) {
      if (path.includes("/admin/Adhome")) {
        this.selectedKeys = ["1"];
      } else if (path.includes("/admin/Adusercontroller")) {
        this.selectedKeys = ["2"];
      } else if (path.includes("/admin/Admoviecontroller")) {
        this.selectedKeys = ["3"];
      } else if (path.includes("/admin/Addirector")) {
        this.selectedKeys = ["4"];
      } else if (path.includes("/admin/Adactor")) {
        this.selectedKeys = ["5"];
      }
    }
  }
};
function _sfc_render$2(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_CodeSandboxOutlined = resolveComponent("CodeSandboxOutlined");
  const _component_a_menu_item = resolveComponent("a-menu-item");
  const _component_user_outlined = resolveComponent("user-outlined");
  const _component_video_camera_outlined = resolveComponent("video-camera-outlined");
  const _component_TeamOutlined = resolveComponent("TeamOutlined");
  const _component_a_menu = resolveComponent("a-menu");
  const _component_a_layout_sider = resolveComponent("a-layout-sider");
  return openBlock(), createBlock(_component_a_layout_sider, {
    collapsed: $props.collapsed,
    trigger: null,
    collapsible: ""
  }, {
    default: withCtx(() => [
      _cache[11] || (_cache[11] = createBaseVNode("div", { class: "Adname" }, [
        createBaseVNode("p", null, "电影时刻")
      ], -1)),
      createVNode(_component_a_menu, {
        selectedKeys: $data.selectedKeys,
        "onUpdate:selectedKeys": _cache[5] || (_cache[5] = ($event) => $data.selectedKeys = $event),
        theme: "light",
        mode: "inline",
        class: "Asidemenu"
      }, {
        default: withCtx(() => [
          createVNode(_component_a_menu_item, {
            key: "1",
            onClick: _cache[0] || (_cache[0] = ($event) => _ctx.$router.push({ path: "/admin/Adhome" }))
          }, {
            default: withCtx(() => [
              createVNode(_component_CodeSandboxOutlined),
              _cache[6] || (_cache[6] = createBaseVNode("span", null, "仪表盘", -1))
            ]),
            _: 1
          }),
          createVNode(_component_a_menu_item, {
            key: "2",
            onClick: _cache[1] || (_cache[1] = ($event) => _ctx.$router.push({ path: "/admin/Adusercontroller" }))
          }, {
            default: withCtx(() => [
              createVNode(_component_user_outlined),
              _cache[7] || (_cache[7] = createBaseVNode("span", null, "用户管理", -1))
            ]),
            _: 1
          }),
          createVNode(_component_a_menu_item, {
            key: "3",
            onClick: _cache[2] || (_cache[2] = ($event) => _ctx.$router.push({ path: "/admin/Admoviecontroller" }))
          }, {
            default: withCtx(() => [
              createVNode(_component_video_camera_outlined),
              _cache[8] || (_cache[8] = createBaseVNode("span", null, "电影管理", -1))
            ]),
            _: 1
          }),
          createVNode(_component_a_menu_item, {
            key: "4",
            onClick: _cache[3] || (_cache[3] = ($event) => _ctx.$router.push({ path: "/admin/Addirector" }))
          }, {
            default: withCtx(() => [
              createVNode(_component_TeamOutlined),
              _cache[9] || (_cache[9] = createBaseVNode("span", null, "导演管理", -1))
            ]),
            _: 1
          }),
          createVNode(_component_a_menu_item, {
            key: "5",
            onClick: _cache[4] || (_cache[4] = ($event) => _ctx.$router.push({ path: "/admin/Adactor" }))
          }, {
            default: withCtx(() => [
              createVNode(_component_TeamOutlined),
              _cache[10] || (_cache[10] = createBaseVNode("span", null, "演员管理", -1))
            ]),
            _: 1
          })
        ]),
        _: 1
      }, 8, ["selectedKeys"])
    ]),
    _: 1
  }, 8, ["collapsed"]);
}
const Aside = /* @__PURE__ */ _export_sfc(_sfc_main$2, [["render", _sfc_render$2]]);
const _sfc_main$1 = defineComponent({
  components: {
    MenuUnfoldOutlined,
    MenuFoldOutlined,
    PoweroffOutlined
  },
  props: {
    collapsed: {
      type: Boolean,
      required: true
    }
  },
  emits: ["update:collapsed"],
  methods: {
    toggleCollapsed() {
      this.$emit("update:collapsed", !this.collapsed);
    },
    tomovie() {
      this.$router.push({ path: "/" }).then(() => {
        window.location.reload();
      });
    }
  }
});
const _hoisted_1$1 = { class: "Aheader" };
const _hoisted_2 = { class: "headerIconDemo" };
function _sfc_render$1(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_menu_unfold_outlined = resolveComponent("menu-unfold-outlined");
  const _component_menu_fold_outlined = resolveComponent("menu-fold-outlined");
  const _component_PoweroffOutlined = resolveComponent("PoweroffOutlined");
  const _component_a_layout_header = resolveComponent("a-layout-header");
  const _component_a_layout = resolveComponent("a-layout");
  return openBlock(), createElementBlock("div", _hoisted_1$1, [
    createVNode(_component_a_layout, null, {
      default: withCtx(() => [
        createVNode(_component_a_layout_header, { style: { "background": "#fff", "padding": "0" } }, {
          default: withCtx(() => [
            createBaseVNode("div", _hoisted_2, [
              _ctx.collapsed ? (openBlock(), createBlock(_component_menu_unfold_outlined, {
                key: 0,
                class: "trigger",
                onClick: _ctx.toggleCollapsed
              }, null, 8, ["onClick"])) : (openBlock(), createBlock(_component_menu_fold_outlined, {
                key: 1,
                class: "trigger",
                onClick: _ctx.toggleCollapsed
              }, null, 8, ["onClick"])),
              createVNode(_component_PoweroffOutlined, {
                class: "trigger",
                onClick: _ctx.tomovie,
                style: { marginRight: "40px" }
              }, null, 8, ["onClick"])
            ])
          ]),
          _: 1
        })
      ]),
      _: 1
    })
  ]);
}
const Aheader = /* @__PURE__ */ _export_sfc(_sfc_main$1, [["render", _sfc_render$1]]);
const _sfc_main = {
  name: "Admin",
  components: {
    Aside,
    Aheader
  },
  data() {
    return {
      collapsed: false
    };
  },
  methods: {
    handleCollapsed(val) {
      this.collapsed = val;
    }
  }
};
const _hoisted_1 = { class: "common-layout" };
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_Aside = resolveComponent("Aside");
  const _component_el_aside = resolveComponent("el-aside");
  const _component_Aheader = resolveComponent("Aheader");
  const _component_el_header = resolveComponent("el-header");
  const _component_router_view = resolveComponent("router-view");
  const _component_el_main = resolveComponent("el-main");
  const _component_el_container = resolveComponent("el-container");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createVNode(_component_el_container, null, {
      default: withCtx(() => [
        createVNode(_component_el_aside, {
          style: normalizeStyle({ width: $data.collapsed ? "4.7vw" : "11.7vw" })
        }, {
          default: withCtx(() => [
            createVNode(_component_Aside, { collapsed: $data.collapsed }, null, 8, ["collapsed"])
          ]),
          _: 1
        }, 8, ["style"]),
        createVNode(_component_el_container, null, {
          default: withCtx(() => [
            createVNode(_component_el_header, null, {
              default: withCtx(() => [
                createVNode(_component_Aheader, {
                  collapsed: $data.collapsed,
                  "onUpdate:collapsed": $options.handleCollapsed
                }, null, 8, ["collapsed", "onUpdate:collapsed"])
              ]),
              _: 1
            }),
            createVNode(_component_el_main, null, {
              default: withCtx(() => [
                createVNode(_component_router_view, { collapsed: $data.collapsed }, null, 8, ["collapsed"])
              ]),
              _: 1
            })
          ]),
          _: 1
        })
      ]),
      _: 1
    })
  ]);
}
const Admin = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render]]);
export {
  Admin as default
};
