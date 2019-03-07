// pages/bmi_cal/bmi_cal.js
var app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    bmiTable: [
      {
        "statu": "",
        "suggest": ""
      },
      {
        "statu": "偏瘦",
        "suggest": "亲，您应注意饮食平衡，适当增加高蛋白质和脂肪（猪肉、牛肉、鸡肉、蛋类、奶类、豆类）摄入，增加体重改善身体状况。"
      },
      {
        "statu": "非常标准",
        "suggest": "亲，您要继续保持正常健康的饮食习惯，通过适当的运动维持健康状态，并进一步完善身体的线条及健康质量。"
      },
      {
        "statu": "过重",
        "suggest": "亲，您日常中应多吃蔬菜水果、高膳食纤维及高营养低热量的食物，同时通过适当的有氧运动加速脂肪和热量的消耗，减低体重，恢复你的自信身段。"
      },
      {
        "statu": "肥胖",
        "suggest": "亲，您要切记：少吃零食、油腻、油炸等高热量食物，要坚定信心的每天坚持1个小时以上的中等强度运动，并管住自己的嘴，合理饮食进行减肥，希望早日恢复优美身段哦。"
      }
    ],
    height:'',

    weight:'',

    standardWeight:'',

    index: 0,
    
    bmi: 0
  },

  /**
   * 查看历史
   */
  getHistory() {
    wx.navigateTo({
      url: '/pages/bmi_history/bmi_history',
    })
  },

  /**
   * 重新计算
   */
  afresh() {
    this.setData({
      height: '',
      weight: ''
    })
  },

  /**
   * 计算
   */
  calculate() {
    var height = this.data.height;
    var weight = this.data.weight;
    if (height < 50 || weight < 5) {
      wx.showToast({
        title: '请正确填写您的数据信息！',
        icon: 'none'
      })
    } else {
      // 服务端保存记录
      wx.request({
        url: domain + '/bmiRecord/save',
        data: {
          token: app.globalData.token,
          height: height,
          weight: weight
        },
        success(res) {
          console.log(res.data);
        }
      })

      var bmi = (weight / (height * height / 10000)).toFixed(1);
      var standardWeight = (22 * height * height / 10000).toFixed(1) + "KG";
      var index = 0;
      if(bmi<=18.4) {
        index = 1;
      } else if(bmi<=23.9) {
        index = 2;
      } else if(bmi<=27.9) {
        index = 3;
      } else if(bmi>=28.0) {
        index = 4;
      }
      this.setData({
        bmi: bmi,
        index: index,
        standardWeight: standardWeight
      })
    }
  },

  heightBindinput(e) {
    this.setData({
      height: e.detail.value
    })
  },

  weightBindinput(e) {
    this.setData({
      weight: e.detail.value
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