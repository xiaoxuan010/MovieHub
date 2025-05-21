import { _ as _export_sfc, k as delete_default, N as Navbar, c as createElementBlock, a as createVNode, b as createBaseVNode, r as resolveComponent, w as withCtx, i as ElMessageBox, e as axios, f as api, E as ElMessage, A as API_PREFIX, o as openBlock, t as toDisplayString, l as createBlock, F as Fragment, h as renderList, m as createCommentVNode, d as withKeys, g as createTextVNode } from "./index-BHtkpZle.js";
const _sfc_main = {
  name: "Moviedetail",
  components: {
    Navbar,
    Delete: delete_default
  },
  data() {
    return {
      movieId: localStorage.getItem("movieId"),
      // 从localStorage获取电影ID
      dialogVisible: false,
      // 控制 dialog 显示
      vipDialogVisible: false,
      // 控制 vip dialog 显示
      dialogTitle: "",
      // 用于动态设置 Dialog 的标题
      dialogMovieTitle: "",
      // 动态设置标题
      movivecomments: [],
      // 评论列表
      currentUserdata: {},
      // 当前用户数据
      addcommentContent: "",
      // 评论内容
      commentshow: true,
      // 控制评论区显示
      teamdetails: {
        // 全部主创的信息
        teamAllname: [],
        teamAllphoto: [],
        teamAllid: [],
        teamdirectorlen: 0,
        teamAlldescription: []
      },
      selectedPerson: {
        // 选择主创个人信息
        name: "",
        photo: "",
        description: "",
        movies: {
          title: [],
          coverImage: [],
          id: [],
          isVip: []
        }
      },
      movieDetail: {
        id: "",
        // 电影ID
        title: "",
        // 电影名称
        description: "",
        // 电影描述
        releaseDate: "",
        // 电影上映日期
        duration: "",
        // 电影时长
        coverImage: "",
        // 电影海报URL
        region: "",
        // 电影地区
        isVip: false,
        // 是否VIP
        score: "",
        // 电影评分
        movieTypes: [],
        // 电影类型
        directors: [],
        // 导演
        actors: [],
        // 演员列表
        playUrl: ""
        // 播放链接
      }
      // 用于存储电影详情
    };
  },
  mounted() {
  },
  created() {
    this.getMovieDetail();
    this.getMovieUrl();
    this.getUserId();
  },
  methods: {
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
    },
    addComment(addcommentContent) {
      axios.post(
        api.apiaddMovieComment,
        {
          movieId: this.movieId,
          content: addcommentContent
        },
        {
          headers: {
            Authorization: "Bearer " + localStorage.getItem("token")
          }
        }
      ).then((response) => {
        if (response.data.code == 200) {
          ElMessage.success("评论成功");
          this.addcommentContent = "";
          this.getMovieComments(this.currentUserdata.id);
        } else {
          ElMessage.error("评论失败");
        }
      });
    },
    deleteComment(commentId) {
      axios.delete(api.apideleteMovieComment(commentId), {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token")
        }
      }).then((response) => {
        if (response.data.code == 200) {
          ElMessage.success("删除成功");
          this.getMovieComments(this.currentUserdata.id);
        } else {
          ElMessage.error("删除失败");
        }
      });
    },
    getUserId() {
      axios.get(api.apigetUsermessage, {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token")
        }
      }).then((response) => {
        if (response.data.code !== 200) {
          ElMessage({
            message: response.data.message,
            type: "error"
          });
          return;
        }
        this.currentUserdata = response.data.data;
        return this.getMovieComments(this.currentUserdata.id);
      });
    },
    getMovieComments(id) {
      axios.get(api.apigetMovieComments(this.movieId), {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token")
        }
      }).then((response) => {
        if (response.data.code !== 200) {
          ElMessage({
            message: response.data.message,
            type: "error"
          });
          return;
        }
        this.movivecomments = response.data.data.records;
        if (response.data.data.total == 0) {
          this.commentshow = false;
        } else {
          this.commentshow = true;
        }
        this.movivecomments.forEach((comment) => {
          if (comment.userId === id) {
            comment.commentdeleteshow = true;
          } else {
            comment.commentdeleteshow = false;
          }
        });
        this.movivecomments.forEach((comment) => {
          const date = new Date(comment.createTime);
          comment.createTime = date.toLocaleString();
        });
      });
    },
    getMovieDetail() {
      axios.get(api.apimovieDetail(this.movieId)).then((response) => {
        this.movieDetail = response.data.data;
        if (Array.isArray(this.movieDetail.directors)) {
          for (let i = 0; i < this.movieDetail.directors.length; i++) {
            const director = this.movieDetail.directors[i];
            if (!this.teamdetails.teamAllname.includes(
              director.name
            )) {
              this.teamdetails.teamAllname.push(director.name);
              this.teamdetails.teamAllphoto.push(
                API_PREFIX + director.photo
              );
              this.teamdetails.teamAlldescription.push(
                director.description
              );
              this.teamdetails.teamAllid.push(director.id);
              this.teamdetails.teamdirectorlen = i + 1;
            }
          }
        }
        if (Array.isArray(this.movieDetail.actors)) {
          for (let i = 0; i < this.movieDetail.actors.length; i++) {
            const actor = this.movieDetail.actors[i];
            if (!this.teamdetails.teamAllname.includes(
              actor.name
            )) {
              this.teamdetails.teamAllname.push(actor.name);
              this.teamdetails.teamAllphoto.push(
                API_PREFIX + actor.photo
              );
              this.teamdetails.teamAlldescription.push(
                actor.description
              );
              this.teamdetails.teamAllid.push(actor.id);
            }
          }
        }
        this.movieDetail.directors = this.movieDetail.directors.map(
          (director) => director.name
        );
        this.movieDetail.actors = this.movieDetail.actors.map(
          (actor) => actor.name
        );
        this.movieDetail.movieTypes = this.movieDetail.movieTypes.map(
          (movietype) => movietype.name
        );
      }).catch((error) => {
        console.error("Error fetching movie details:", error);
      });
    },
    getMovieUrl() {
      axios.get(api.apimovieVideoUrl(this.movieId), {
        headers: {
          Authorization: "Bearer " + localStorage.getItem("token")
        }
      }).then((response) => {
        if (response.data.code !== 200) {
          ElMessage({
            message: response.data.message,
            type: "error"
          });
          return;
        }
        this.movieDetail.playUrl = API_PREFIX + response.data.data.videoUrl;
      }).catch((error) => {
        ElMessage({
          message: "获取视频地址失败，请稍后再试",
          type: "error"
        });
      });
    },
    moreteamatedetail(index) {
      const id = Number(index) + 1;
      this.selectedPerson = {
        name: this.teamdetails.teamAllname[index],
        photo: this.teamdetails.teamAllphoto[index],
        description: this.teamdetails.teamAlldescription[index],
        movies: {
          title: [],
          coverImage: [],
          id: [],
          isVip: []
        }
      };
      if (id <= this.teamdetails.teamdirectorlen) {
        this.dialogTitle = "导演信息";
        this.dialogMovieTitle = "执导电影";
        axios.get(
          api.apigetMoviesByDirector(
            this.teamdetails.teamAllid[index]
          )
        ).then((response) => {
          let records = response.data.data.records;
          for (let i = 0; i < records.length; i++) {
            this.selectedPerson.movies.title[i] = records[i].title;
            this.selectedPerson.movies.coverImage[i] = records[i].coverImage;
            this.selectedPerson.movies.id[i] = records[i].id;
            this.selectedPerson.movies.isVip[i] = records[i].isVip;
          }
        }).catch((error) => {
          console.error("Error fetching director details:", error);
        });
      } else {
        this.dialogTitle = "演员信息";
        this.dialogMovieTitle = "参演电影";
        axios.get(
          api.apigetMoviesByActor(
            this.teamdetails.teamAllid[index]
          )
        ).then((response) => {
          let records = response.data.data.records;
          for (let i = 0; i < records.length; i++) {
            this.selectedPerson.movies.title[i] = records[i].title;
            this.selectedPerson.movies.coverImage[i] = records[i].coverImage;
            this.selectedPerson.movies.id[i] = records[i].id;
            this.selectedPerson.movies.isVip[i] = records[i].isVip;
          }
        }).catch((error) => {
          console.error("Error fetching actor details:", error);
        });
      }
      this.dialogVisible = true;
    },
    handledialogClose() {
      this.selectedPerson = {
        name: "",
        photo: "",
        description: "",
        movies: {
          title: [],
          coverImage: [],
          id: [],
          isVip: []
        }
      };
      this.dialogVisible = false;
    },
    gotoMovieDetailPage(movieId, isVip) {
      localStorage.setItem("movieId", movieId);
      const userType = Number(localStorage.getItem("userType")) || 0;
      if (userType === 0 && isVip === 1) {
        if (localStorage.getItem("token") == null) {
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
      this.$router.go(0);
    },
    returnPage() {
      this.$router.go(-1);
    }
  }
};
const _hoisted_1 = { class: "moviedetaildemo" };
const _hoisted_2 = { class: "moviedetailcontent" };
const _hoisted_3 = { class: "mdcontainer" };
const _hoisted_4 = { class: "md-header" };
const _hoisted_5 = { class: "md-content" };
const _hoisted_6 = { class: "md-title" };
const _hoisted_7 = { class: "md-detailcontent" };
const _hoisted_8 = { class: "md-description" };
const _hoisted_9 = { class: "md-line" };
const _hoisted_10 = { class: "md-text" };
const _hoisted_11 = { class: "md-detailtext" };
const _hoisted_12 = { class: "md-text" };
const _hoisted_13 = { class: "md-detailtext" };
const _hoisted_14 = { class: "md-text" };
const _hoisted_15 = { class: "md-detailtext" };
const _hoisted_16 = { class: "md-line" };
const _hoisted_17 = { class: "md-text" };
const _hoisted_18 = { class: "md-detailtext" };
const _hoisted_19 = { class: "md-text" };
const _hoisted_20 = { class: "md-detailtext" };
const _hoisted_21 = { class: "md-text" };
const _hoisted_22 = { class: "md-detailtext" };
const _hoisted_23 = { class: "md-line" };
const _hoisted_24 = { class: "md-text" };
const _hoisted_25 = { class: "md-detailtext" };
const _hoisted_26 = { class: "md-text" };
const _hoisted_27 = { class: "md-detailtext" };
const _hoisted_28 = { class: "md-text" };
const _hoisted_29 = { class: "md-detailtext" };
const _hoisted_30 = ["src"];
const _hoisted_31 = { class: "md-movieshow" };
const _hoisted_32 = ["src"];
const _hoisted_33 = { class: "md-commenttile" };
const _hoisted_34 = { class: "md-commentime" };
const _hoisted_35 = { class: "md-commenttext" };
const _hoisted_36 = { class: "md-movieteam" };
const _hoisted_37 = ["onClick"];
const _hoisted_38 = ["src"];
const _hoisted_39 = { class: "md-team-name" };
const _hoisted_40 = { class: "mdmate-info" };
const _hoisted_41 = { class: "mdmate-textcontent" };
const _hoisted_42 = ["src"];
const _hoisted_43 = { class: "mdmate-name" };
const _hoisted_44 = { class: "mdmate-description" };
const _hoisted_45 = { class: "mdmate-moviecontent" };
const _hoisted_46 = { class: "mdmate-moviecontent-title" };
const _hoisted_47 = { class: "mdmate-movies" };
const _hoisted_48 = ["onClick"];
const _hoisted_49 = ["src"];
const _hoisted_50 = { class: "md-team-name" };
const _hoisted_51 = { style: { "text-align": "center" } };
function _sfc_render(_ctx, _cache, $props, $setup, $data, $options) {
  const _component_Navbar = resolveComponent("Navbar");
  const _component_CloseBold = resolveComponent("CloseBold");
  const _component_el_icon = resolveComponent("el-icon");
  const _component_el_col = resolveComponent("el-col");
  const _component_el_row = resolveComponent("el-row");
  const _component_Delete = resolveComponent("Delete");
  const _component_el_button = resolveComponent("el-button");
  const _component_el_tooltip = resolveComponent("el-tooltip");
  const _component_el_scrollbar = resolveComponent("el-scrollbar");
  const _component_el_empty = resolveComponent("el-empty");
  const _component_el_input = resolveComponent("el-input");
  const _component_el_dialog = resolveComponent("el-dialog");
  const _component_el_card = resolveComponent("el-card");
  return openBlock(), createElementBlock("div", _hoisted_1, [
    createVNode(_component_Navbar),
    createBaseVNode("div", _hoisted_2, [
      createBaseVNode("div", _hoisted_3, [
        createVNode(_component_el_card, { class: "mdbox-card" }, {
          default: withCtx(() => [
            createBaseVNode("div", _hoisted_4, [
              _cache[7] || (_cache[7] = createBaseVNode("p", { class: "md-demotitle" }, "电影详情", -1)),
              createVNode(_component_el_icon, {
                size: "25",
                class: "md-closeIcon",
                onClick: $options.returnPage
              }, {
                default: withCtx(() => [
                  createVNode(_component_CloseBold)
                ]),
                _: 1
              }, 8, ["onClick"])
            ]),
            createBaseVNode("div", _hoisted_5, [
              createBaseVNode("p", _hoisted_6, toDisplayString(this.movieDetail.title), 1),
              createBaseVNode("div", _hoisted_7, [
                createBaseVNode("div", null, [
                  createBaseVNode("p", _hoisted_8, toDisplayString(this.movieDetail.description), 1),
                  createBaseVNode("div", _hoisted_9, [
                    createBaseVNode("div", _hoisted_10, [
                      _cache[8] || (_cache[8] = createBaseVNode("p", { class: "md-detailtile" }, "片名", -1)),
                      createBaseVNode("p", _hoisted_11, toDisplayString(this.movieDetail.title), 1)
                    ]),
                    createBaseVNode("div", _hoisted_12, [
                      _cache[9] || (_cache[9] = createBaseVNode("p", { class: "md-detailtile" }, "上映时间", -1)),
                      createBaseVNode("p", _hoisted_13, toDisplayString(this.movieDetail.releaseDate), 1)
                    ]),
                    createBaseVNode("div", _hoisted_14, [
                      _cache[10] || (_cache[10] = createBaseVNode("p", { class: "md-detailtile" }, "时长", -1)),
                      createBaseVNode("p", _hoisted_15, toDisplayString(this.movieDetail.duration), 1)
                    ])
                  ]),
                  createBaseVNode("div", _hoisted_16, [
                    createBaseVNode("div", _hoisted_17, [
                      _cache[11] || (_cache[11] = createBaseVNode("p", { class: "md-detailtile" }, "地区", -1)),
                      createBaseVNode("p", _hoisted_18, toDisplayString(this.movieDetail.region), 1)
                    ]),
                    createBaseVNode("div", _hoisted_19, [
                      _cache[12] || (_cache[12] = createBaseVNode("p", { class: "md-detailtile" }, "导演", -1)),
                      createBaseVNode("p", _hoisted_20, toDisplayString(this.movieDetail.directors.join(
                        "/ "
                      )), 1)
                    ]),
                    createBaseVNode("div", _hoisted_21, [
                      _cache[13] || (_cache[13] = createBaseVNode("p", { class: "md-detailtile" }, "评分", -1)),
                      createBaseVNode("p", _hoisted_22, toDisplayString(this.movieDetail.score), 1)
                    ])
                  ]),
                  createBaseVNode("div", _hoisted_23, [
                    createBaseVNode("div", _hoisted_24, [
                      _cache[14] || (_cache[14] = createBaseVNode("p", { class: "md-detailtile" }, "演员", -1)),
                      createBaseVNode("p", _hoisted_25, toDisplayString(this.movieDetail.actors.join(
                        "/ "
                      )), 1)
                    ]),
                    createBaseVNode("div", _hoisted_26, [
                      _cache[15] || (_cache[15] = createBaseVNode("p", { class: "md-detailtile" }, "类型", -1)),
                      createBaseVNode("p", _hoisted_27, toDisplayString(this.movieDetail.movieTypes.join(
                        "/ "
                      )), 1)
                    ]),
                    createBaseVNode("div", _hoisted_28, [
                      _cache[16] || (_cache[16] = createBaseVNode("p", { class: "md-detailtile" }, "权限", -1)),
                      createBaseVNode("p", _hoisted_29, toDisplayString(this.movieDetail.isVip ? "Vip电影" : "普通电影"), 1)
                    ])
                  ])
                ]),
                createBaseVNode("img", {
                  src: this.movieDetail.coverImage,
                  alt: "电影海报",
                  style: { "width": "12vw", "height": "15vw" }
                }, null, 8, _hoisted_30)
              ]),
              createBaseVNode("div", _hoisted_31, [
                createVNode(_component_el_row, null, {
                  default: withCtx(() => [
                    createVNode(_component_el_col, { span: 12 }, {
                      default: withCtx(() => _cache[17] || (_cache[17] = [
                        createBaseVNode("p", { class: "md-demotitle" }, "电影正片", -1)
                      ])),
                      _: 1
                    }),
                    createVNode(_component_el_col, { span: 12 }, {
                      default: withCtx(() => _cache[18] || (_cache[18] = [
                        createBaseVNode("p", { class: "md-demotitle" }, "评论区", -1)
                      ])),
                      _: 1
                    })
                  ]),
                  _: 1
                }),
                createVNode(_component_el_row, null, {
                  default: withCtx(() => [
                    createVNode(_component_el_col, { span: 12 }, {
                      default: withCtx(() => [
                        createBaseVNode("video", {
                          src: this.movieDetail.playUrl,
                          controls: "",
                          style: { "width": "95%", "height": "100%", "text-align": "center" }
                        }, null, 8, _hoisted_32)
                      ]),
                      _: 1
                    }),
                    createVNode(_component_el_col, { span: 12 }, {
                      default: withCtx(() => [
                        $data.commentshow ? (openBlock(), createBlock(_component_el_scrollbar, {
                          key: 0,
                          style: { "height": "40vh", "width": "100%" }
                        }, {
                          default: withCtx(() => [
                            (openBlock(true), createElementBlock(Fragment, null, renderList($data.movivecomments, (comment, index) => {
                              return openBlock(), createElementBlock("div", {
                                class: "md-commenttext",
                                key: index
                              }, [
                                createVNode(_component_el_row, { style: { "height": "15px" } }, {
                                  default: withCtx(() => [
                                    createVNode(_component_el_col, { span: 21 }, {
                                      default: withCtx(() => [
                                        createBaseVNode("p", _hoisted_33, " 用户 " + toDisplayString(comment.username), 1)
                                      ]),
                                      _: 2
                                    }, 1024),
                                    createVNode(_component_el_col, { span: 3 }, {
                                      default: withCtx(() => [
                                        comment.commentdeleteshow ? (openBlock(), createBlock(_component_el_tooltip, {
                                          key: 0,
                                          content: "删除评论",
                                          placement: "top"
                                        }, {
                                          default: withCtx(() => [
                                            createVNode(_component_el_button, {
                                              class: "md-commentdelete",
                                              type: "danger",
                                              circle: "",
                                              size: "small",
                                              onClick: ($event) => $options.deleteComment(
                                                comment.id
                                              )
                                            }, {
                                              default: withCtx(() => [
                                                createVNode(_component_el_icon, null, {
                                                  default: withCtx(() => [
                                                    createVNode(_component_Delete)
                                                  ]),
                                                  _: 1
                                                })
                                              ]),
                                              _: 2
                                            }, 1032, ["onClick"])
                                          ]),
                                          _: 2
                                        }, 1024)) : createCommentVNode("", true)
                                      ]),
                                      _: 2
                                    }, 1024)
                                  ]),
                                  _: 2
                                }, 1024),
                                createVNode(_component_el_row, null, {
                                  default: withCtx(() => [
                                    createVNode(_component_el_col, null, {
                                      default: withCtx(() => [
                                        createBaseVNode("p", _hoisted_34, toDisplayString(comment.createTime), 1),
                                        createBaseVNode("p", _hoisted_35, toDisplayString(comment.content), 1)
                                      ]),
                                      _: 2
                                    }, 1024)
                                  ]),
                                  _: 2
                                }, 1024)
                              ]);
                            }), 128))
                          ]),
                          _: 1
                        })) : (openBlock(), createBlock(_component_el_empty, {
                          key: 1,
                          description: "description"
                        })),
                        createVNode(_component_el_row, {
                          justify: "center",
                          align: "middle"
                        }, {
                          default: withCtx(() => [
                            createVNode(_component_el_col, { span: 20 }, {
                              default: withCtx(() => [
                                createVNode(_component_el_input, {
                                  type: "textarea",
                                  placeholder: "请输入评论内容",
                                  modelValue: $data.addcommentContent,
                                  "onUpdate:modelValue": _cache[0] || (_cache[0] = ($event) => $data.addcommentContent = $event),
                                  onKeyup: _cache[1] || (_cache[1] = withKeys(($event) => $options.addComment(
                                    $data.addcommentContent
                                  ), ["enter", "native"]))
                                }, null, 8, ["modelValue"])
                              ]),
                              _: 1
                            }),
                            createVNode(_component_el_col, {
                              span: 4,
                              style: { "display": "flex", "justify-content": "center", "align-items": "center" }
                            }, {
                              default: withCtx(() => [
                                createVNode(_component_el_button, {
                                  type: "primary",
                                  onClick: _cache[2] || (_cache[2] = ($event) => $options.addComment(
                                    $data.addcommentContent
                                  ))
                                }, {
                                  default: withCtx(() => _cache[19] || (_cache[19] = [
                                    createTextVNode(" 评论 ")
                                  ])),
                                  _: 1
                                })
                              ]),
                              _: 1
                            })
                          ]),
                          _: 1
                        })
                      ]),
                      _: 1
                    })
                  ]),
                  _: 1
                })
              ]),
              createBaseVNode("div", _hoisted_36, [
                _cache[22] || (_cache[22] = createBaseVNode("p", { class: "md-teamtext" }, "主创团队", -1)),
                (openBlock(true), createElementBlock(Fragment, null, renderList($data.teamdetails.teamAllname, (teammate, index) => {
                  return openBlock(), createElementBlock("div", {
                    class: "md-team-item",
                    key: index,
                    onClick: ($event) => $options.moreteamatedetail(index)
                  }, [
                    createBaseVNode("img", {
                      src: $data.teamdetails.teamAllphoto[index],
                      alt: "主创头像",
                      class: "md-team-photo"
                    }, null, 8, _hoisted_38),
                    createBaseVNode("p", _hoisted_39, toDisplayString(teammate), 1)
                  ], 8, _hoisted_37);
                }), 128)),
                createVNode(_component_el_dialog, {
                  title: $data.dialogTitle,
                  modelValue: $data.dialogVisible,
                  "onUpdate:modelValue": _cache[3] || (_cache[3] = ($event) => $data.dialogVisible = $event),
                  draggable: true,
                  "header-class": "mdmate-title",
                  onClose: $options.handledialogClose
                }, {
                  default: withCtx(() => [
                    createBaseVNode("div", _hoisted_40, [
                      createBaseVNode("div", _hoisted_41, [
                        createBaseVNode("div", null, [
                          createBaseVNode("img", {
                            src: $data.selectedPerson.photo,
                            alt: "人物头像",
                            class: "mdmate-photo"
                          }, null, 8, _hoisted_42)
                        ]),
                        createBaseVNode("div", null, [
                          createBaseVNode("p", _hoisted_43, toDisplayString($data.selectedPerson.name), 1),
                          createBaseVNode("p", _hoisted_44, toDisplayString($data.selectedPerson.description), 1)
                        ])
                      ]),
                      createBaseVNode("div", _hoisted_45, [
                        createBaseVNode("p", _hoisted_46, toDisplayString($data.dialogMovieTitle), 1),
                        createBaseVNode("div", _hoisted_47, [
                          (openBlock(true), createElementBlock(Fragment, null, renderList($data.selectedPerson.movies.title, (teammate, index) => {
                            return openBlock(), createElementBlock("div", {
                              class: "md-team-item",
                              key: index,
                              onClick: ($event) => $options.gotoMovieDetailPage(
                                $data.selectedPerson.movies.id[index],
                                $data.selectedPerson.movies.isVip[index]
                              )
                            }, [
                              createBaseVNode("img", {
                                src: $data.selectedPerson.movies.coverImage[index],
                                alt: "电影海报",
                                class: "md-movie-photo"
                              }, null, 8, _hoisted_49),
                              createBaseVNode("p", _hoisted_50, toDisplayString(teammate), 1)
                            ], 8, _hoisted_48);
                          }), 128))
                        ])
                      ])
                    ])
                  ]),
                  _: 1
                }, 8, ["title", "modelValue", "onClose"]),
                createVNode(_component_el_dialog, {
                  title: "选择会员类型",
                  modelValue: $data.vipDialogVisible,
                  "onUpdate:modelValue": _cache[6] || (_cache[6] = ($event) => $data.vipDialogVisible = $event),
                  width: "300px"
                }, {
                  default: withCtx(() => [
                    createBaseVNode("div", _hoisted_51, [
                      createVNode(_component_el_button, {
                        type: "primary",
                        onClick: _cache[4] || (_cache[4] = ($event) => $options.payVip("monthly"))
                      }, {
                        default: withCtx(() => _cache[20] || (_cache[20] = [
                          createTextVNode("月度会员")
                        ])),
                        _: 1
                      }),
                      createVNode(_component_el_button, {
                        type: "warning",
                        onClick: _cache[5] || (_cache[5] = ($event) => $options.payVip("yearly")),
                        style: { "margin-left": "20px" }
                      }, {
                        default: withCtx(() => _cache[21] || (_cache[21] = [
                          createTextVNode("年度会员")
                        ])),
                        _: 1
                      })
                    ])
                  ]),
                  _: 1
                }, 8, ["modelValue"])
              ])
            ])
          ]),
          _: 1
        })
      ])
    ])
  ]);
}
const Moviedetail = /* @__PURE__ */ _export_sfc(_sfc_main, [["render", _sfc_render]]);
export {
  Moviedetail as default
};
