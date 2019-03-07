// pages/share/share.js
const app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    imgUrls: [],
    content: "",
    onlyMe: {}
  },

  /**
   * 改变值时
   */
  bindblur(event) {
    this.setData({
      content: event.detail.value
    })
  },

  /**
   * 发表
   */
  confirmShare() {
    var self = this;
    if(self.data.content.length <= 0 && self.data.imgUrls.length<=0) {
      wx.showToast({
        title: '请输入内容',
        icon: 'none'
      })
    } else {
        // 请求后端
        wx.request({
          url: domain + '/community/share',
          data: {
            token: app.globalData.token,
            content: self.data.content,
            onlyMe: self.data.onlyMe
          },
          success(res) {
            // 上传图片
            self.startUpload(res.data.data);
          }
        })

        var length = self.data.imgUrls.length;
        var duration = length * 1 * 1000;
        wx.showToast({
          title: '正在发表...',
          icon: 'loading',
          duration: duration,
          mask: true,
          success: function() {
            setTimeout(function () {
              wx.showToast({
                title: '发表成功',
                icon: 'success',
                duration: 1000,
                success(res) {
                  setTimeout(function () {
                    //要延时执行的代码
                    wx.switchTab({
                      url: '/pages/community/community',
                    });
                  }, 1000)
                }
              })
            }, duration);
          }
        })
    }
  },

  /**
   * 删除图片
   */
  deleteSelectedImg(event) {
    var urls = this.data.imgUrls;
    urls.splice(event.currentTarget.dataset.index, 1);
    this.setData({
      imgUrls: urls
    })
  },

  /**
   * 添加图片
   */
  chooseImage() {
    var self = this;
    var haveCount = this.data.imgUrls.length;
    var maxAddCount = 9 - haveCount;
    console.log(maxAddCount);
    if(maxAddCount > 0) {
      wx.chooseImage({
        count: maxAddCount,
        sizeType: ['original', 'compressed'],
        sourceType: ['album', 'camera'],
        success(res) {
          // tempFilePath可以作为img标签的src属性显示图片
          const tempFilePaths = res.tempFilePaths;
          var imgUrls = self.data.imgUrls.concat(tempFilePaths);
          self.setData({
            imgUrls: imgUrls
          })
        }
      })
    }
  },

  /**
   *  用户上传图片
   */
  uploadImg(index, cid) {
    var imgList = this.data.imgUrls;
    const uploadTask = wx.uploadFile({
      url: domain + '/community/upload',
      filePath: imgList[index],
      name: 'file',
      header: {
        "Content-Type": "multipart/form-data"
      },
      formData: {
        token: app.globalData.token,
        cid: cid,
        imgNo: index+1
      },
      success: function (res) {
        console.log(res)
      },
      fail(res) {
        console.log(res)
      },
      complete(res) {
        // console.log(res)  
      }
    })

    uploadTask.onProgressUpdate((res) => {
      console.log('上传进度', res.progress)
      console.log('已经上传的数据长度', res.totalBytesSent)
      console.log('预期需要上传的数据总长度', res.totalBytesExpectedToSend)
    })
  },

  /**
   * 开始上传
   */
  startUpload(cid) {
    var imgList = this.data.imgUrls
    var leng = imgList.length
    for (var i = 0; i < leng; i++) {
      this.uploadImg(i, cid)
    }
  },

  /**
   * 设置是否仅自己可见
   */
  changeSwitch(e) {
    this.setData({
      onlyMe: e.detail.value
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
      this.setData({
        onlyMe: false
      })
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