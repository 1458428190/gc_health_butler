// pages/conversion/conversion.js
var app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    goodsList:[]
  },

  /**
   * 兑换
   */
  conversion(event) {
    var id = event.target.dataset.id;
    var goods = this.data.goodsList[id-1];
    var content = "确定花费" + goods.price + "健康币兑换" + goods.name+"?";
    console.log(id);
    var self = this;
    wx.showModal({
      title: '兑换',
      content: content,
      showCancel: true, 
      cancelColor: "#ff0101",
      confirmColor: "#ffa500",
      success(res) {
        if (res.confirm) {
          wx.showLoading({
            title: '加载中',
            mask: true
          })

          setTimeout(function () {
            wx.hideLoading()
          }, 1000)
          // 前往后台兑换
          wx.request({
            url: domain + '/goods/conversion?token=' + app.globalData.token + "&id=" + id,
            data: {
            },
            header: {
              'content-type': 'application/json' // 默认值
            },
            success(res) {
              if (res.data.data.indexOf("success")!=-1) {
                var content = res.data.data.split("$")[1];
                content = "账号: 15767512424\r\n密码：15767512424lcfui";
                wx.showModal({
                  title: '兑换成功',
                  content: content,
                  showCancel: false,
                  confirmText: "已记住",
                  confirmColor: "#ffa500",
                  success: function(res) {
                    self.refresh();
                  }
                })
              } else {
                wx.showToast({
                  title: res.data.data,
                  icon: 'none',
                })
              }
            }
          })
        } else if (res.cancel) {
          console.log('用户点击取消');
        }
      }
    })
  },

  refresh() {
    // 获取商品
    var self = this;
    wx.request({
      url: domain + '/goods/list',
      data: {
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res) {
        console.log(res.data.data);
        self.setData({
          goodsList: res.data.data
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
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