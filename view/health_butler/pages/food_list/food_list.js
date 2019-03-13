var app = getApp();
const domain = app.globalData.domain;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    inputValue: '',
    isSearch: 1,
    cid: 0,
    foodList: [],
    briefLength: 29,
    keyword: '',
    noResTip: '',
    myOptions: {}
  },

  /**
   * 获取食物详情
   */
  getFoodDetail(event) {
    var fid = event.currentTarget.dataset.id;
    var imgurl = event.currentTarget.dataset.imgurl;
    var name = event.currentTarget.dataset.name;
    var url = '/pages/food_detail/food_detail?fid=' + fid + '&imgurl=' + imgurl + '&name=' + name;
    console.log("url ", url)
    wx.navigateTo({
      url: url
    })
  },

  /**
   * 查找
   */
  search(event) {
    var keyword = event.detail.value;
    var cid = this.data.cid;
    wx.navigateTo({
      url: '/pages/food_list/food_list?fcid=' + cid + '&keyword=' + keyword,
    })

  },

  /**
   * 输入数据时
   */
  searchInput(event) {
    console.log(JSON.stringify(event));
    var cursor = event.detail.cursor;
    if (cursor > 0) {
      this.setData({
        isSearch: 0
      })
    } else {
      this.setData({
        isSearch: 1
      })
    }
  },

  /**
   * 点击取消
   */
  cancel(event) {
    this.setData({
      isSearch: 1,
      inputValue: ''
    })
  },

  /**
   * 返回主页
   */
  backhome() {
    wx.switchTab({
      url: '/pages/food/food'
    })
  },

  refresh() {
    var options = this.data.myOptions;
    var keyword = "";
    let fcid = options.fcid;
    if (options.keyword != undefined) {
      keyword = decodeURIComponent(options.keyword);
    }
    let self = this;
    // url += "fcid=0";
    keyword = (keyword == undefined ? "" : keyword);
    var duration = keyword==""?1000:3500;
    wx.showLoading({
      title: '加载中',
      mask: true
    })
    setTimeout(function () {
      wx.hideLoading()
    }, duration);
    
    fcid = (fcid == undefined ? 0 : fcid);
    console.log("foodList > keyword: ", keyword);
    self.setData({
      noResTip: ''
    })

    if (keyword != undefined) {
      this.setData({
        inputValue: keyword,
        isSearch: 0
      })
    }
    if (fcid != undefined) {
      this.setData({
        cid: fcid
      })
    } else {
      this.setData({
        cid: 0
      })
    }

    wx.request({
      url: domain + "/food/list",
      data: {
        keyword: keyword,
        fcid: fcid
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res) {
        self.setData({
          foodList: res.data.data
        });
        if (res.data.length <= 0) {
          self.setData({
            noResTip: '无结果'
          })
        }
      }
    })
  },

  /** 
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.setData({
      myOptions: options
    })
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