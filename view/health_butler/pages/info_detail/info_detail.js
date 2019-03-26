// pages/community/community.js
const app = getApp();
const domain = app.globalData.domain;
const size = 20;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    communityVo: {},

    inputBoxShow: false,

    userData: app.globalData.userData,

    height: 0,

    height_01: 0,

    height_02: 0,

    cid: 0,

    toUid: 0,

    commentContent: "",

    toName: ""
  },

  /**
   * 点击回复时显示评论框
   */
  replayVisibleComment(event) {
    console.log("replayVisibleComment", event);
    var toUid = event.currentTarget.dataset.toid;
    var toName = event.currentTarget.dataset.toname;
    this.setData({
      inputBoxShow: true,
      toUid: toUid,
      toName: toName
    });
  },

  /**
   * 回复评论
   */
  reply() {
    var that = this;
    // 评论
    var cid = that.data.cid;
    var commentContent = that.data.commentContent;
    var toUid = that.data.toUid;
    var toName = that.data.toName;
    // 直接加进前端
    var communityVo = this.data.communityVo;
    wx.request({
      url: domain + '/communityRecord/replay',
      data: {
        token: app.globalData.token,
        cid: cid,
        content: commentContent,
        toUid: toUid
      },
      success(res) {
      }
    })
    var commentList = communityVo.commentList;
    commentList.push({
      "fromId": that.data.userData.id,
      "fromName": "我",
      "toId": toUid,
      "toName": toName,
      "content": commentContent
    });
    var express =  "communityVo.commentList";
    console.log("express", express);
    this.setData({
      inputBoxShow: false,
      [express]: commentList
    });
    this.refresh();
  },

  /**
   * 长按删除键
   */
  longpressDelete(event) {
    console.log("长按删除");
    var rid = event.currentTarget.dataset.rid;
    var toId = event.currentTarget.dataset.toid;
    if (toId === this.userData.user.id) {
      var self = this;
      wx.showModal({
        title: '提示',
        content: '是否确定删除',
        cancelColor: "#1ECBBE",
        confirmColor: "#1ECBBE",
        success(res) {
          if (res.confirm) {
            console.log('用户点击确定');
            wx.request({
              url: domain + '/communityRecord/deleteComment',
              data: {
                token: app.globalData.token,
                rid: rid
              },
              success(res) {
                wx.showToast({
                  title: '删除成功',
                  icon: 'success',
                  duration: 500
                })
              }
            })

            // 前端直接展示删除结果
            var longpressIndex = event.currentTarget.dataset.longpressindex;
            var commentIndex = event.currentTarget.dataset.index;
            var communityVo = this.data.communityVo;
            var commentList = communityVo.commentList;
            commentList.splice(longpressIndex, 1);
            var express = "communityVo.commentList";
            self.setData({
              [express]: commentList
            });

          } else if (res.cancel) {
            console.log('用户点击取消')
          }
        }
      })
    }
  },

  /**
   * 评论或回复
   */
  comment(event) {
    var that = this;
    var toUid = that.data.toUid;
    // 回复
    if (toUid != '') {
      that.reply();
      return;
    }

    // 评论
    var cid = that.data.cid;
    var commentContent = that.data.commentContent;
    // 直接加进前端
    var communityVo = that.data.communityVo;
    console.log("communityVO", communityVo);
    wx.request({
      url: domain + '/communityRecord/comment',
      data: {
        token: app.globalData.token,
        cid: cid,
        content: commentContent
      },
      success(res) {
      }
    })
    var commentList = communityVo.commentList;
    commentList.push({
      "fromId": that.data.userData.id,
      "fromName": "我",
      "toId": 0,
      "toName": "",
      "content": commentContent
    });
    var express = "communityVo.commentList";
    this.setData({
      inputBoxShow: false,
      [express]: commentList
    });
    this.refresh();
  },

  bindinput(event) {
    this.setData({
      commentContent: event.detail.value
    })
  },

  /**
   * 用于取消事件冒泡
   */
  noOp() {

  },

  bindfocus: function (e) {
    let that = this;
    let height = 0;
    let height_02 = 0;
    wx.getSystemInfo({
      success: function (res) {
        height_02 = res.windowHeight;
      }
    })
    height = e.detail.height - (that.data.height_01 - height_02);
    console.log('app is', app.globalData.height_01);
    that.setData({
      height: height,
    })
    console.log('获得焦点的 e is', e);
  },
  //监听input失去焦点
  bindblur: function (e) {
    this.setData({
      height: 0,
      inputShow: false,
    });
    console.log('失去焦点的 e is', e);
  },

  /**
   * 显示评论框
   */
  visibleComment(event) {
    this.setData({
      inputBoxShow: true,
      toUid: ''
    });
  },

  /**
   * 隐藏评论框
   */
  inVisibleComment() {
    this.setData({
      inputBoxShow: false,
      commentContent: ""
    });
  },

  /**
   * 访问
   */
  visit(event) {
    var toUid = event.currentTarget.dataset.uid;
    console.log("event ", event);
    wx.navigateTo({
      url: '/pages/home_page/home_page?toUid=' + toUid,
    })
  },

  /** 
   * 预览图片
   */
  previewImage: function (e) {
    var current = e.target.dataset.src;
    var urlList = e.target.dataset.list;
    wx.previewImage({
      current: current, // 当前显示图片的http链接
      urls: urlList // 需要预览的图片http链接列表
    })
  },

  /**
   * 取消点赞
   */
  cancel(event) {
    var cid = this.data.cid;
    var self = this;
    // 后端
    wx.request({
      url: domain + '/communityRecord/praise',
      data: {
        token: app.globalData.token,
        cid: cid,
        type: 0
      },
      success(res) {
        // 刷新 
        self.refresh();
      }
    })
  },
  /**
   * 点赞
   */
  praise(event) {
    var cid = this.data.cid;
    var self = this;
    // 后端
    wx.request({
      url: domain + '/communityRecord/praise',
      data: {
        token: app.globalData.token,
        cid: cid,
        type: 1
      },
      success(res) {
        // 刷新 
        self.refresh();
      }
    })
  },

  /**
   * 删除
   */
  delete(event) {
    var cid = this.data.cid;
    var self = this;
    wx.showModal({
      title: '提示',
      content: '确定删除此动态？',
      cancelColor: "#1ECBBE",
      confirmColor: "#1ECBBE",
      success(res) {
        if (res.confirm) {
          console.log('用户点击确定');
          wx.showLoading({
            title: '加载中',
            mask: true
          })
          setTimeout(function () {
            wx.hideLoading()
          }, 1000)
          wx.request({
            url: domain + '/community/delete',
            data: {
              token: app.globalData.token,
              cid: cid
            },
            success(res) {
              wx.showLoading({
                title: '加载中',
                mask: true
              })
              setTimeout(function () {
                wx.hideLoading()
              }, 1000);
              self.refresh();
            }
          })
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    })
  },

  refresh() {
    var cid = this.data.cid;
    var self = this;
    wx.request({
      url: domain + '/community/getByCid',
      data: {
        token: app.globalData.token,
        cid: cid
      },
      success(res) {
        self.setData({
          communityVo: res.data.data
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
  
    var height_01 = 0;
    wx.getSystemInfo({
      success: function (res) {
        height_01 = res.windowHeight;
      }
    })

    this.setData({
      user: app.globalData.userInfo,
      userData: app.globalData.userData,
      height_01: height_01,
      cid: options.cid
    })

    wx.showLoading({
      title: '加载中',
      mask: true
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 1000);

    this.refresh();
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    this.setData({
      isMe: false,
      user: app.globalData.userInfo
    })
    // 刷新 
    this.refresh();
    wx.showLoading({
      title: '加载中',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 1200)
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})