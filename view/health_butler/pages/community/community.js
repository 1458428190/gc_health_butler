// pages/community/community.js
const app = getApp();
const domain = app.globalData.domain;
const size = 20;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    allShare: [],

    meShare: [],

    isMe: false,

    user: app.globalData.userInfo,

    userData: app.globalData.userData,

    noMore: false,

    pageNo: 1,

    inputBoxShow: false,

    height: 0,

    height_01: 0,
    
    height_02: 0,

    cid: 0,

    toUid: 0,

    commentContent:"",

    commentIndex: -1,

    toName: "",

    unReadInfoCount: 0
  },

  /**
   * 获取未读消息
   */
  getUnReadInfo() {
    wx.navigateTo({
      url: '/pages/info_record/info_record',
      success() {
        console.log('跳转成功');
      }
    })
  },

  /**
   * 点击回复时显示评论框
   */
  replayVisibleComment(event) {
    console.log("replayVisibleComment", event);
    var cid = event.currentTarget.dataset.cid;
    var toUid = event.currentTarget.dataset.toid;
    var toName = event.currentTarget.dataset.toname;
    var commentIndex = event.currentTarget.dataset.index;
    this.setData({
      inputBoxShow: true,
      cid: cid,
      toUid: toUid,
      toName: toName,
      commentIndex: commentIndex
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
    var commentIndex = that.data.commentIndex;
    // 直接加进前端
    var communityVo = that.data.isMe ? that.data.meShare[commentIndex] : that.data.allShare[commentIndex];
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
    var express = (that.data.isMe ? "meShare[" : "allShare[") + commentIndex + "].commentList";
    console.log("express", express);
    this.setData({
      inputBoxShow: false,
      [express]: commentList
    });
  },

  /**
   * 刷新
   */
  upper() {
    console.log("上拉刷新");
    this.refresh();
    wx.showLoading({
      title: '加载中',
      mask: true
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 1000)
  },

  /**
   * 长按删除键
   */
  longpressDelete(event) {
    console.log("长按删除");
    var rid = event.currentTarget.dataset.rid;
    var toId = event.currentTarget.dataset.toid;
    if(toId === this.data.userData.user.id) {
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
            var commentIndex = event.currentTarget.dataset.index;
            var longpressIndex = event.currentTarget.dataset.longpressindex;
            var communityVo = self.data.isMe ? self.data.meShare[commentIndex] : self.data.allShare[commentIndex];
            var commentList = communityVo.commentList;
            commentList.splice(longpressIndex, 1);
            var express = (self.data.isMe ? "meShare[" : "allShare[") + commentIndex + "].commentList";
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
    var commentIndex = that.data.commentIndex;
    // 直接加进前端
    var communityVo = that.data.isMe?that.data.meShare[commentIndex] : that.data.allShare[commentIndex];
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
    var express = (that.data.isMe?"meShare[":"allShare[")+commentIndex+"].commentList";
    this.setData({ 
      inputBoxShow: false,
      [express]: commentList
     });
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
    height = e.detail.height - 49;
    console.log('app is h1', app.globalData.height_01, ' h2 ', height_02, ' height ', e.detail.height);
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
    var cid = event.currentTarget.dataset.cid;
    var commentIndex = event.currentTarget.dataset.index;
    this.setData({ 
      inputBoxShow: true,
      cid: cid,
      commentIndex: commentIndex,
      toUid: ''
     });
  },

  /**
   * 隐藏评论框
   */
  inVisibleComment() {
    this.setData({ inputBoxShow: false,
    commentContent: "" });
  },

  /**
   * 访问
   */
  visit(event) {
    var toUid = event.currentTarget.dataset.uid;
    console.log("event ", event);
    wx.navigateTo({
      url: '/pages/home_page/home_page?toUid='+toUid,
    })
  },

  /**
   * 加载更多
   */
  lower() {
    console.log("下拉到底");
    if(this.data.noMore) {
      return;
    }
    var pageNo = this.data.pageNo + 1;
    var self = this;
    wx.request({
      url: domain + '/community/list_size',
      data: {
        token: app.globalData.token
      },
      success(res) {
        // 已经没有更多了
        if (self.data.allShare.length >= res.data.data) {
          self.setData({
            noMore: true
          })
        } else {
          self.getCommunity(pageNo);
        }
      }
    })
    
    wx.showLoading({
      title: '加载中',
      mask: true
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 800)
  },

  getCommunity(pageNo) {
    var self = this;
    // 获取动态
    wx.request({
      url: domain + '/community/pageList',
      data: {
        token: app.globalData.token,
        pageNo: pageNo,
        size: size
      },
      success(res) {
        var allShare = self.data.allShare;
        if (pageNo != 1) {
          allShare = allShare.concat(res.data.data);
        } else {
          allShare = res.data.data;
        }
        self.setData({
          pageNo: pageNo,
          allShare: allShare
        })
      }
    });
  },

  /**
   * 发表
   */
  share() {
    wx.navigateTo({
      url: '/pages/share/share',
      success() {
        console.log('跳转成功');
      }
    })
  },

  /** 
   * 预览图片
   */
  previewImage: function (e) {  
    var current=e.target.dataset.src;
    var urlList=e.target.dataset.list;
    wx.previewImage({
        current: current, // 当前显示图片的http链接
        urls: urlList // 需要预览的图片http链接列表
    })
  },

  /**
   * 查看自己发表的历史
   */
  visitMe() {
    this.setData({
      share: this.data.meShare,
      isMe: true
    })
  },

  /**
   * 返回
   */
  back() {
    this.setData({
      isMe: false,
      share: this.data.allShare
    })
  },

  /**
   * 取消点赞
   */
  cancel(event) {
    var cid = event.currentTarget.dataset.cid;
    var index = event.currentTarget.dataset.index;
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
      }
    })

    var communityVo = this.data.isMe ? this.data.meShare[index] : this.data.allShare[index];
    var praiseUser = communityVo.praiseUser;
    for(var i=0; i<praiseUser.length; i++) {
      if(praiseUser[i].id == this.data.userData.user.id) {
        console.log("取消点赞");
        praiseUser.splice(i, 1);
        break;
      }
    }
    communityVo.praise = false;
    console.log("communityVO", communityVo);
    var express = (this.data.isMe ? "meShare[" : "allShare[") + index + "]";
    this.setData({
      [express]: communityVo
    });

  },
  /**
   * 点赞
   */
  praise(event) {
    var cid = event.currentTarget.dataset.cid;
    var index = event.currentTarget.dataset.index;
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
      }
    })

    var communityVo = this.data.isMe ? this.data.meShare[index] : this.data.allShare[index];
    var praiseUser = communityVo.praiseUser;
    praiseUser.push({
      "id":this.data.userData.user.id,
      "nickName": this.data.userData.user.nickName
    })
    communityVo.praise = true;
    console.log("communityVO", communityVo);
    var express = (this.data.isMe ? "meShare[" : "allShare[") + index + "]";
    this.setData({
      [express]: communityVo
    });
  },

  /**
   * 刷新
   */
  refresh() {
    // 获取健康小圈动态
    var self = this;
    wx.request({
      url: domain + '/community/pageList',
      data: {
        token: app.globalData.token,
        pageNo: 1,
        size: size
      },
      success(res) {
        self.setData({
          allShare: res.data.data,
          pageNo: 1
        })
      }
    });

    // 获取自己的健康动态
    wx.request({
      url: domain + '/community/getMe',
      data: {
        token: app.globalData.token
      },
      success(res) {
        self.setData({
          meShare: res.data.data
        })
      }
    });

    // 获取有关于自己的未读消息个数
    wx.request({
      url: domain + '/infoRecord/getUnReadInfoCount',
      data: {
        token: app.globalData.token
      },
      success(res) {
        self.setData({
          unReadInfoCount: res.data.data
        })
      }
    })
  },

  /**
   * 删除
   */
  delete(event) {
    var cid = event.currentTarget.dataset.cid;
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

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      user: app.globalData.userInfo,
      userData: app.globalData.userData,
    })
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
      user: app.globalData.userInfo,
      userData: app.globalData.userData
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