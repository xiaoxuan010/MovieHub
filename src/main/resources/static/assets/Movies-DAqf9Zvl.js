import { _ as _export_sfc, N as Navbar, c as createElementBlock, a as createVNode, b as createBaseVNode, r as resolveComponent, n as normalizeClass, F as Fragment, h as renderList, w as withCtx, e as axios, f as api, i as ElMessageBox, j as emitter, o as openBlock, t as toDisplayString, g as createTextVNode } from "./index-BHtkpZle.js";
const _sfc_main = {
  name: "Movies",
  components: {
    Navbar
  },
  data() {
    return {
      selected: [0, 0, 0],
      movies: [],
      isSearchMode: false,
      searchQuery: "",
      currentPage: 1,
      selectmovieType: [1, 2, 3, 4, 5, 23],
      pageSize: 24,
      total: 0,
      vipDialogVisible: false,
      currentTab: "all"
      // 新增
    };
  },
  computed: {
    // 每行8个，分三行
    movieRows() {
      const rows = [];
      for (let i = 0; i < 3; i++) {
        rows.push(this.movies.slice(i * 8, (i + 1) * 8));
      }
      return rows;
    }
  },
  mounted() {
    emitter.on("search-result", this.handleSearchResult);
    const query = this.$route.query.query;
    if (query) {
      this.isSearchMode = true;
      this.searchQuery = query;
      this.currentPage = 1;
      this.searchMovies();
    } else {
      this.getAllMovies();
    }
  },
  beforeUnmount() {
    emitter.off("search-result", this.handleSearchResult);
  },
  methods: {
    getAllMovies() {
      axios.get(api.apigetAllMovies, {
        params: {
          current: this.currentPage,
          size: this.pageSize
        }
      }).then((res) => {
        if (res.data.code === 200) {
          this.movies = res.data.data.records;
          this.total = res.data.data.total;
          this.currentPage = res.data.data.current;
          this.pageSize = res.data.data.size;
        }
      });
    },
    getAllMoviesByRanking(sortBy) {
      axios.get(api.apigetAllMoviesByRanking, {
        params: {
          sortBy,
          order: "desc",
          current: this.currentPage,
          size: this.pageSize
        }
      }).then((res) => {
        if (res.data.code === 200) {
          this.movies = res.data.data.records;
          this.total = res.data.data.total;
          this.currentPage = res.data.data.current;
          this.pageSize = res.data.data.size;
        }
      });
    },
    getMoviesBytype(type) {
      axios.get(api.apigetMoviesByType(type), {
        params: {
          current: this.currentPage,
          size: this.pageSize
        }
      }).then((res) => {
        if (res.data.code === 200) {
          this.movies = res.data.data.records;
          this.total = res.data.data.total;
          this.currentPage = res.data.data.current;
          this.pageSize = res.data.data.size;
        }
      });
    },
    getMoviesByViptype(type) {
      axios.get(api.apigetMoviesByVip(type), {
        params: {
          current: this.currentPage,
          size: this.pageSize
        }
      }).then((res) => {
        if (res.data.code === 200) {
          this.movies = res.data.data.records;
          this.total = res.data.data.total;
          this.currentPage = res.data.data.current;
          this.pageSize = res.data.data.size;
        }
      });
    },
    handleCurrentChange(page) {
      this.currentPage = page;
      if (this.isSearchMode) {
        this.searchMovies();
      } else if (this.currentTab === "score") {
        this.getAllMoviesByRanking("score");
      } else if (this.currentTab === "play_count") {
        this.getAllMoviesByRanking("play_count");
      } else if (this.currentTab === "type") {
        this.getMoviesBytype(this.selectmovieType[this.selected[1]]);
      } else if (this.currentTab === "viptype") {
        this.getMoviesByViptype(this.selected[2]);
      } else {
        this.getAllMovies();
      }
    },
    handleSizeChange(size) {
      this.pageSize = size;
      this.currentPage = 1;
      if (this.isSearchMode) {
        this.searchMovies();
      } else if (this.currentTab === "score") {
        this.getAllMoviesByRanking("score");
      } else if (this.currentTab === "play_count") {
        this.getAllMoviesByRanking("play_count");
      } else if (this.currentTab === "type") {
        this.getMoviesBytype(this.selectmovieType[this.selected[1]]);
      } else if (this.currentTab === "viptype") {
        this.getMoviesByViptype(this.selected[2]);
      } else {
        this.getAllMovies();
      }
    },
    handleSearchResult(payload) {
      this.isSearchMode = true;
      this.searchQuery = payload.query;
      this.movies = payload.movies;
      this.total = payload.total;
      this.currentPage = payload.currentPage;
      this.pageSize = payload.pageSize;
    },
    searchMovies() {
      axios.get(api.apiSearchMovies, {
        params: {
          query: this.searchQuery,
          current: this.currentPage,
          size: this.pageSize
        }
      }).then((res) => {
        if (res.data.code === 200) {
          this.movies = res.data.data.records;
          this.total = res.data.data.total;
          this.currentPage = res.data.data.current;
          this.pageSize = res.data.data.size;
        }
      });
    },
    goToMovieDetail(movieId, isVip) {
      localStorage.setItem("movieId", movieId);
      const userType = Number(localStorage.getItem("userType")) || 0;
      const movieType = isVip;
      if (userType == 0 && movieType == 1) {
        if (!localStorage.getItem("token")) {
          ElMessageBox.confirm("您还未登录，该电影需要会员", "提示", {
            confirmButtonText: "登录",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            this.$router.push("/login");
          });
        } else {
          ElMessageBox.confirm(
            "您是普通用户，该电影需要会员",
            "提示",
            {
              confirmButtonText: "去开通",
              cancelButtonText: "取消",
              type: "warning"
            }
          ).then(() => {
            this.vipDialogVisible = true;
          });
        }
        return;
      }
      this.$router.push("/movieDetail");
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
const _hoisted_1 = { class: "moviecontent" };
const _hoisted_2 = { class: "movies-nav" };
const _hoisted_3 = { class: "movies-navitem" };
const _hoisted_4 = { class: "movies-navitem" };
const _hoisted_5 = { class: "movies-navitem" };
const _hoisted_6 = { class: "movies-shower" };
const _hoisted_7 = ["onClick"];
const _hoisted_8 = ["src"];
const _hoisted_9 = ["onClick"];
const _hoisted_10 = ["src"];
const _hoisted_11 = { class: "movies-title" };
const _hoisted_12 = { class: "movies-pagination" };
const _hoisted_13 = {
  div: "",
  style: { "display": "flex", "justify-content": "center", "align-items": "center", "height": "80px" }
};
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_Navbar = resolveComponent("Navbar");
  const _component_el_badge = resolveComponent("el-badge");
  const _component_el_pagination = resolveComponent("el-pagination");
  const _component_el_button = resolveComponent("el-button");
  const _component_el_dialog = resolveComponent("el-dialog");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createVNode(_component_Navbar),
    createBaseVNode("div", _hoisted_2, [
      createBaseVNode("div", _hoisted_3, [
        createBaseVNode("a", {
          class: normalizeClass(["movies-text", { active: $data.selected[0] === 0 }]),
          onClick: _cache[0] || (_cache[0] = ($event) => ($data.selected[0] = 0, $data.currentTab = "all", $data.currentPage = 1, $data.isSearchMode = false, $options.getAllMovies()))
        }, "全部", 2),
        createBaseVNode("a", {
          class: normalizeClass(["movies-text", { active: $data.selected[0] === 1 }]),
          onClick: _cache[1] || (_cache[1] = ($event) => ($data.selected[0] = 1, $data.currentTab = "score", $data.currentPage = 1, $data.isSearchMode = false, $options.getAllMoviesByRanking("score")))
        }, "高分好评", 2),
        createBaseVNode("a", {
          class: normalizeClass(["movies-text", { active: $data.selected[0] === 2 }]),
          onClick: _cache[2] || (_cache[2] = ($event) => ($data.selected[0] = 2, $data.currentTab = "play_count", $data.currentPage = 1, $data.isSearchMode = false, $options.getAllMoviesByRanking("play_count")))
        }, "播放热门", 2)
      ]),
      createBaseVNode("div", _hoisted_4, [
        createBaseVNode("a", {
          class: normalizeClass(["movies-text", { active: $data.selected[1] === 0 }]),
          onClick: _cache[3] || (_cache[3] = ($event) => ($data.selected[1] = 0, $data.currentTab = "all", $data.currentPage = 1, $data.isSearchMode = false, $options.getAllMovies()))
        }, "类型", 2),
        createBaseVNode("a", {
          class: normalizeClass(["movies-text", { active: $data.selected[1] === 1 }]),
          onClick: _cache[4] || (_cache[4] = ($event) => ($data.selected[1] = 1, $data.currentTab = "type", $data.currentPage = 1, $data.isSearchMode = false, $options.getMoviesBytype($data.selectmovieType[0])))
        }, "动作", 2),
        createBaseVNode("a", {
          class: normalizeClass(["movies-text", { active: $data.selected[1] === 2 }]),
          onClick: _cache[5] || (_cache[5] = ($event) => ($data.selected[1] = 2, $data.currentTab = "type", $data.currentPage = 1, $data.isSearchMode = false, $options.getMoviesBytype($data.selectmovieType[1])))
        }, "传记", 2),
        createBaseVNode("a", {
          class: normalizeClass(["movies-text", { active: $data.selected[1] === 3 }]),
          onClick: _cache[6] || (_cache[6] = ($event) => ($data.selected[1] = 3, $data.currentTab = "type", $data.currentPage = 1, $data.isSearchMode = false, $options.getMoviesBytype($data.selectmovieType[2])))
        }, "犯罪", 2),
        createBaseVNode("a", {
          class: normalizeClass(["movies-text", { active: $data.selected[1] === 4 }]),
          onClick: _cache[7] || (_cache[7] = ($event) => ($data.selected[1] = 4, $data.currentTab = "type", $data.currentPage = 1, $data.isSearchMode = false, $options.getMoviesBytype($data.selectmovieType[3])))
        }, "剧情", 2),
        createBaseVNode("a", {
          class: normalizeClass(["movies-text", { active: $data.selected[1] === 5 }]),
          onClick: _cache[8] || (_cache[8] = ($event) => ($data.selected[1] = 5, $data.currentTab = "type", $data.currentPage = 1, $data.isSearchMode = false, $options.getMoviesBytype($data.selectmovieType[4])))
        }, "爱情", 2),
        createBaseVNode("a", {
          class: normalizeClass(["movies-text", { active: $data.selected[1] === 6 }]),
          onClick: _cache[9] || (_cache[9] = ($event) => ($data.selected[1] = 6, $data.currentTab = "type", $data.currentPage = 1, $data.isSearchMode = false, $options.getMoviesBytype($data.selectmovieType[5])))
        }, "神话", 2)
      ]),
      createBaseVNode("div", _hoisted_5, [
        createBaseVNode("a", {
          class: normalizeClass(["movies-text", { active: $data.selected[2] === 0 }]),
          onClick: _cache[10] || (_cache[10] = ($event) => ($data.selected[2] = 0, $data.currentTab = "all", $data.currentPage = 1, $data.isSearchMode = false, $options.getAllMovies()))
        }, "资费", 2),
        createBaseVNode("a", {
          class: normalizeClass(["movies-text", { active: $data.selected[2] === 1 }]),
          onClick: _cache[11] || (_cache[11] = ($event) => ($data.selected[2] = 1, $data.currentTab = "viptype", $data.currentPage = 1, $data.isSearchMode = false, $options.getMoviesByViptype(0)))
        }, "免费", 2),
        createBaseVNode("a", {
          class: normalizeClass(["movies-text", { active: $data.selected[2] === 2 }]),
          onClick: _cache[12] || (_cache[12] = ($event) => ($data.selected[2] = 2, $data.currentTab = "viptype", $data.currentPage = 1, $data.isSearchMode = false, $options.getMoviesByViptype(1)))
        }, "会员", 2)
      ])
    ]),
    createBaseVNode("div", _hoisted_6, [
      (openBlock(true), createElementBlock(Fragment, null, renderList($options.movieRows, (row, rowIndex) => {
        return openBlock(), createElementBlock("div", {
          class: "movies-row",
          key: rowIndex
        }, [
          (openBlock(true), createElementBlock(Fragment, null, renderList(row, (movie) => {
            return openBlock(), createElementBlock("div", {
              class: "movies-card",
              key: movie.id
            }, [
              movie.isVip ? (openBlock(), createElementBlock("div", {
                key: 0,
                onClick: ($event) => $options.goToMovieDetail(movie.id, movie.isVip)
              }, [
                createVNode(_component_el_badge, {
                  class: "item",
                  value: "Vip",
                  offset: [-25, 9],
                  color: "#663D00"
                }, {
                  default: withCtx(() => [
                    createBaseVNode("img", {
                      class: "movies-cover",
                      src: movie.coverImage,
                      alt: "封面"
                    }, null, 8, _hoisted_8)
                  ]),
                  _: 2
                }, 1024)
              ], 8, _hoisted_7)) : (openBlock(), createElementBlock("div", {
                key: 1,
                onClick: ($event) => ($options.goToMovieDetail(movie.id), movie.isVip)
              }, [
                createBaseVNode("img", {
                  class: "movies-cover",
                  src: movie.coverImage,
                  alt: "封面"
                }, null, 8, _hoisted_10)
              ], 8, _hoisted_9)),
              createBaseVNode("div", _hoisted_11, toDisplayString(movie.title), 1)
            ]);
          }), 128))
        ]);
      }), 128)),
      createBaseVNode("div", _hoisted_12, [
        createVNode(_component_el_pagination, {
          "current-page": $data.currentPage,
          "onUpdate:currentPage": _cache[13] || (_cache[13] = ($event) => $data.currentPage = $event),
          "page-size": $data.pageSize,
          "onUpdate:pageSize": _cache[14] || (_cache[14] = ($event) => $data.pageSize = $event),
          background: "",
          layout: "prev, pager, next, jumper",
          total: $data.total,
          onSizeChange: $options.handleSizeChange,
          onCurrentChange: $options.handleCurrentChange
        }, null, 8, ["current-page", "page-size", "total", "onSizeChange", "onCurrentChange"])
      ]),
      createVNode(_component_el_dialog, {
        title: "选择会员类型",
        modelValue: $data.vipDialogVisible,
        "onUpdate:modelValue": _cache[17] || (_cache[17] = ($event) => $data.vipDialogVisible = $event),
        width: "300px",
        "align-center": ""
      }, {
        default: withCtx(() => [
          createBaseVNode("div", _hoisted_13, [
            createVNode(_component_el_button, {
              type: "primary",
              onClick: _cache[15] || (_cache[15] = ($event) => $options.payVip("monthly"))
            }, {
              default: withCtx(() => _cache[18] || (_cache[18] = [
                createTextVNode("月度会员")
              ])),
              _: 1
            }),
            createVNode(_component_el_button, {
              type: "warning",
              onClick: _cache[16] || (_cache[16] = ($event) => $options.payVip("yearly")),
              style: { "margin-left": "20px" }
            }, {
              default: withCtx(() => _cache[19] || (_cache[19] = [
                createTextVNode("年度会员")
              ])),
              _: 1
            })
          ])
        ]),
        _: 1
      }, 8, ["modelValue"])
    ])
  ]);
}
const Movies = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render]]);
export {
  Movies as default
};
