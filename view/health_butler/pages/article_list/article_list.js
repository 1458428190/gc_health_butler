// pages/article/article.js
var wxRequest = require('../../utils/wxRequest.js')
var Promise = require('../../plugins/es6-promise.js')
var app = getApp();
const domain = app.globalData.domain;
const size = 10;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    articleList: {},
    articleCategoryList: {},
    cid: 1,
    articleTmp: [],
    pageNo: 1,
    noMore: false,
    scrollTop: 0
  },

  /**
   * 批量获取指定分类的文章 (TODO 弃用)
   */
  getBatchArticle(cid) {
    var countUrl = domain + '/article/list_size?cid=' + cid;
    var self = this;
    wxRequest.getRequest(countUrl, {}).then(res => {
      var pageCount = parseInt(((res.data + size - 1) / size).toFixed(0));
      var pageList = [];
      for (var page = 1; page <= pageCount; page++) {
        pageList.push(page);
      }

      // 使用prmise数组,批量获取文章
      var promises = pageList.map(function (page) {
        var url = domain + '/article/list?cid=' + cid + "&page=" + page + "&size=" + size;
        return wxRequest.getRequest(url, {});
      });

      Promise.all(promises).then(function (res) {
        console.log(JSON.stringify(res));
        var articleList = [];
        for(var i=0; i<res.length; i++) {
          articleList.push(res[i].data.data);
        }
        self.setData({
          articleList: articleList
        })
      })
      .catch(function (reason) {
        console.error(season);
      });
    });
  },

  /**
   * 获取指定分类的所有文章
   */
  getArticleList(cid, pageNo) {
    var self = this;
    // 获取文章
    wx.request({
      url: domain + '/article/pageList',
      data: {
        cid: cid,
        page: pageNo,
        size: size
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res) {
        var articleList = self.data.articleList;
        if(pageNo != 1) {
          articleList = articleList.concat(res.data.data);
        } else {
          articleList = res.data.data;
        }
        self.setData({
          articleList: articleList,
          cid: cid,
          pageNo: pageNo
        })
      }
    });
  },

  // 底部加载
  lower() {
    var pageNo = this.data.pageNo + 1;
    var self = this;
    wx.request({
      url: domain + '/article/list_size',
      data: {
        cid: self.data.cid
      },
      success(res) {
        // 已经没有更多了
        if (self.data.articleList.length >= res.data.data) {
           self.setData({
             noMore: true
           })
        } else {
          self.getArticleList(self.data.cid, pageNo);
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

  // 顶部刷新
  upper() {

  },

  /**
   * 获取文章详情
   */
  getArticleDetail(event) {
    var id = event.currentTarget.dataset.id;
    var url = '/pages/article_detail/article_detail?id=' + id;
    wx.navigateTo({
      url: url,
    })
  },  

  /**
   * 选择类型
   */
  selectType(event) {
    var cid = event.target.dataset.cid;
    this.getArticleList(cid, 1);
    this.setData({
      noMore: false,
      scrollTop: 0
    })
    wx.showLoading({
      title: '加载中',
      mask: true
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 800)
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var self = this;
    // 获取文章分类
    wx.request({
      url: domain + '/article/category',
      data: {
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res) {
        self.setData({
          articleCategoryList: res.data.data
        })
      }
    });
    this.getArticleList(1, 1);
    wx.showLoading({
      title: '加载中',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 2000)
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
      pageNo: 1
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
    console.log("----------------------")
  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    console.log("++++++++++++++++++++++++")
  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})