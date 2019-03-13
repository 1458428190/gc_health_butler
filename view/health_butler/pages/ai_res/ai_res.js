// pages/ai_res/ai_res.js
const app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imgUrl: "",
    userInfo: app.globalData.userInfo,
    aiSkin: {},
    feature: []
  },

  /**
  *  用户上传图片
  */
  upload(imgUrl) {
    var self = this;
    var duration = 3 * 1000;
    wx.showToast({
      title: '正在上传...',
      icon: 'loading',
      duration: duration,
      mask: true,
      success: function () {
        setTimeout(function () {
          wx.showToast({
            title: '上传成功',
            icon: 'success',
            duration: 1000
          })
        }, duration);
      }
    })
    const uploadTask = wx.uploadFile({
      url: domain + '/aiSkin/measure',
      filePath: imgUrl,
      name: 'file',
      header: {
        "Content-Type": "multipart/form-data;charset=utf-8"
      },
      formData: {
        token: app.globalData.token,
      },
      success(res) {
        var data = JSON.parse(res.data);
        console.log("res", data.data);
        console.log("res message ", data.data.message);
        if(data.data.message != null && data.data.message != undefined && data.data.message.length > 0) {
          wx.showModal({
            title: '提示',
            content: data.data.message,
            showCancel: false,
            confirmText: "重新选择",
            success(res) {
              if (res.confirm) {
                wx.navigateBack({
                  delta: 1
                })
              }
            }
          })
        } else {
          self.handler(data.data);
        }
      }
    })
    uploadTask.onProgressUpdate((res) => {
      console.log('上传进度', res.progress)
      console.log('已经上传的数据长度', res.totalBytesSent)
      console.log('预期需要上传的数据总长度', res.totalBytesExpectedToSend)
    })
  },

  /**
   * 处理拿到的数据
   */
  handler(data) {
    var feature = this.data.feature;
    if(data.emotion != null) {
      feature.push(data.emotion);
    }
    if(data.leftEyeStatus != null) {
      feature.push("左眼"+data.leftEyeStatus);
    }
    if(data.rightEyeStatus != null) {
      feature.push("右眼" + data.rightEyeStatus);
    }
    if (data.smile != null) {
      feature.push(data.smile);
    }
    if (data.ethniCity != null) {
      feature.push(data.ethniCity);
    }
    this.setData({
      aiSkin: data,
      feature: feature
    })
  },

  /**
   * 获取指定id的测肤记录
   */
  inquiry(id) {

    wx.showLoading({
      title: '加载中',
      mask: true
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 1000)

    var self = this;
    wx.request({
      url: domain + '/aiSkin/inquiry',
      data: {
        token: app.globalData.token,
        id: id
      },
      success(res) {
        self.handler(res.data.data);
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var imgUrl = options.imgUrl;
    var id = options.id;
    if(imgUrl != undefined && imgUrl != "") {
      this.upload(imgUrl);
    } else {
      if(id != undefined && id!="") {
        this.inquiry(id);
      }
    }
    this.setData({
      userInfo: app.globalData.userInfo
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