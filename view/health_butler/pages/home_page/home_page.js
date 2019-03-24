import F2 from '../../f2-canvas/lib/f2';
const app = getApp();
const domain = app.globalData.domain;

let chart = null;

function formatNumber(n) {
  return String(Math.floor(n * 100) / 100).replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

Page({

  /**
   * 页面的初始数据
   */
  data: {
    opts: {},

    isMe: {},

    user: {},

    toUid: {},

    runData: [],

    coin: {},

    defaultImg: ["http://ruanjiangongcheng2.xyz:9000/health-butler/cover_1.jpg", "http://ruanjiangongcheng2.xyz:9000/health-butler/cover_2.jpg", "http://ruanjiangongcheng2.xyz:9000/health-butler/cover_3.jpg"],

    showModalStatus: false,
  },

  initChart(canvas, width, height) {
    const originDates = [];
    const data = this.data.runData;
    var length = data.length;
    console.log("initChart ", data);
    // 默认最新的12个
    for(var i=length-12; i<length; i++) {
      originDates.push(data[i].date);
    }

    chart = new F2.Chart({
      el: canvas,
      width,
      height,
      animate: true
    });

    chart.source(data, {
      date: {
        type: 'timeCat',
        tickCount: 5,
        values: originDates,
        mask: 'YY-MM-DD'
      },
      steps: {
        tickCount: 12
      }
    });

    chart.axis('date', {
      tickLine: {
        length: 4,
        //标记点颜色
        stroke: 'green'
      },
      label: {
        // 横坐标颜色
        fill: 'white'
      },
      line: {
        top: false
      }
    });
    chart.axis('steps', {
      position: 'right',
      label(text) {
        return {
          text: formatNumber(text * 1),
          // 纵坐标颜色
          fill: 'white'
        };
      },
      grid: {
        // 横线颜色
        stroke: '#FFF'
      }
    });

    chart.tooltip({
      showItemMarker: true,
      tooltipMarkerStyle: {
        fill: '#000' // 设置 tooltipMarker 的样式
      },
      showTitle: true,
      offsetX: 12, // x 方向的偏移
      offsetY: 12,
      showCrosshairs: true,
      background: {
        radius: 2,
        padding: [3, 5]
      },
      onShow(ev) {
        const items = ev.items;
        items[0].name = '';
        items[0].value = items[0].value + ' 步';
      }
    });
    chart.interval().position('date*steps').style({
      radius: [2, 2, 0, 0]
    }).color("gold"); // 设置柱形颜色

    // 定义进度条
    chart.scrollBar({
      mode: 'x',
      xStyle: {
        // 进度条背景颜色
        backgroundColor: '#e8e8e8',
        // 进度条填充颜色
        // fillerColor: '#808080',
        fillerColor: 'orange',
        offsetY: -1
      }
    });
    chart.interaction('pan');
    chart.render();
    return chart;
  },

  /**
   * 赏币
   */
  bindinput(event) {
    var coin = event.detail.value;
    this.setData({
      coin: coin
    })
  },

  /**
 * 点击打赏
 */
  reward(e) {
    var currentStatu = e.currentTarget.dataset.statu;
    this.util(currentStatu);
  },

  /**
   * 确定打赏
   */
  confirm(e) {
    console.log(" ------------------- ");
    var currentStatu = e.currentTarget.dataset.statu;
    this.util(currentStatu);
    var self = this;
    wx.showLoading({
      title: '加载中',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 1000)

    // 后端请求
    wx.request({
      url: domain + '/user/reward',
      data: {
        token: app.globalData.token,
        toUid: self.data.user.id,
        coin: self.data.coin
      },
      success(res) {
        if(res.data.code == 200) {
          wx.showToast({
            title: '打赏成功',
          })
        } else {
          wx.showToast({
            icon: "none",
            title: res.data.errMsg,
          })
        }
      } 
    })
  },

  /**
   * 取消打赏
   */
  cancel: function (e) {
    var currentStatu = e.currentTarget.dataset.statu;
    this.util(currentStatu)
  },

  util: function (currentStatu) {
    /* 动画部分 */
    // 第1步：创建动画实例   
    var animation = wx.createAnimation({
      duration: 200,  //动画时长  
      timingFunction: "linear", //线性  
      delay: 0  //0则不延迟  
    });

    // 第2步：这个动画实例赋给当前的动画实例  
    this.animation = animation;

    // 第3步：执行第一组动画  
    animation.opacity(0).rotateX(-100).step();

    // 第4步：导出动画对象赋给数据对象储存  
    this.setData({
      animationData: animation.export()
    })

    // 第5步：设置定时器到指定时候后，执行第二组动画  
    setTimeout(function () {
      // 执行第二组动画  
      animation.opacity(1).rotateX(0).step();
      // 给数据对象储存的第一组动画，更替为执行完第二组动画的动画对象  
      this.setData({
        animationData: animation
      })

      //关闭  
      if (currentStatu == "close") {
        this.setData(
          {
            showModalStatus: false
          }
        );
      }
    }.bind(this), 200)

    // 显示  
    if (currentStatu == "open") {
      this.setData(
        {
          showModalStatus: true
        }
      );
    }
  },

  /**
   * 修改封面
   */
  changeCover() {
    if(this.data.isMe) { 
    var self = this;
    wx.chooseImage({
      count: 9,
      sizeType: ['original', 'compressed'],
      sourceType: ['album', 'camera'],
      success(res) {
        // tempFilePath可以作为img标签的src属性显示图片
        const tempFilePaths = res.tempFilePaths;
        const userTemp = self.data.user;
        console.log(JSON.stringify(tempFilePaths));
        userTemp.coverImgUrl = tempFilePaths;
        self.setData({
          user: userTemp
        })
        // 先重置封面
        wx.request({
          url: domain + '/user/cover/reset',
          data: {
            token: app.globalData.token
          },
          success(res) {
            // 开始上传
            var leng = tempFilePaths.length
            for (var i = 0; i < leng; i++) {
              self.uploadImg(i, tempFilePaths[i])
            }
          }
        })
        var duration = 2 * 1000;
        wx.showToast({
          title: '正在更改封面...',
          icon: 'loading',
          duration: duration,
          mask: true,
          success: function () {
            setTimeout(function () {
              wx.showToast({
                title: '更改成功',
                icon: 'success',
                duration: 1000,
                success(res) {
                  wx.request({
                    url: domain + '/user/seeHomePage',
                    data: {
                      token: app.globalData.token
                    },
                    success(res) {
                      console.log("res ", res.data.data);
                      self.setData({
                        user: res.data.data,
                        isMe: true
                      });
                    }
                  });
                }
              })
            }, duration);
          }
        })
      }
    })
    }
  },

  /**
 *  用户上传图片
 */
  uploadImg(index, imgUrl) {
    const uploadTask = wx.uploadFile({
      url: domain + '/user/cover/upload',
      filePath: imgUrl,
      name: 'file',
      header: {
        "Content-Type": "multipart/form-data"
      },
      formData: {
        token: app.globalData.token,
        imgNo: index + 1
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
   * 跳转到健康币详情
   */
  nvToHealthCoinDetail() {
    if(this.data.isMe) {
      wx:wx.navigateTo({
        url: '/pages/health_coin_detail/health_coin_detail'
      })
    }
  },

  /**
   * 跳转到排行榜
   */
  nvToRank() {
    wx: wx.navigateTo({
      url: '/pages/rank/rank'
    })
  },

  /**
   * 获取主页信息
   */
  getHomePage(toUid) {
    var self = this;
    // 重置tip
    this.setData({
      tip: ""
    })
    // 访问主页基本信息
    console.log("app.user.id", app.globalData.userData.user.id);
    console.log("toUid", toUid);
    if(toUid == undefined || toUid == '' || toUid.length <= 0 || toUid == app.globalData.userData.user.id) {
      wx.request({
        url: domain + '/user/seeHomePage',
        data: {
          token: app.globalData.token
        },
        success(res) {
          console.log("res ", res.data.data);
          self.setData({
            user: res.data.data,
            isMe: true
          });
        }
      });

      // 访问步数历史
      wx.request({
        url: domain + '/record/step',
        data: {
          token: app.globalData.token
        },
        success(res) {
          if(res.data.data.length <= 0) {
          } else {
          var initChart = self.initChart;
          self.setData({
            runData: res.data.data,
            'opts.onInit': initChart
          })
          }
        }
      })
    } 
    else {
      wx.request({
        url: domain + '/user/seeHomePage',
        data: {
          token: app.globalData.token,
          toUid: toUid
        },
        success(res) {
          self.setData({
            user: res.data.data,
            isMe: false
          })
        }
      });

      // 访问步数历史
      wx.request({
        url: domain + '/record/step',
        data: {
          token: app.globalData.token,
          toUid: toUid
        },
        success(res) {
          if (res.data.data.length <= 0) {
          } else {
          var initChart = self.initChart;
          self.setData({
            runData: res.data.data,
            'opts.onInit': initChart
          })
          }
        }
      })
    }

    wx.showLoading({
      title: '加载中',
      mask: true
    })

    setTimeout(function () {
      wx.hideLoading()
    }, 2000)
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var toUid = options.toUid;
    console.log("visit ", toUid, " options: ", options);
    this.setData({
      toUid: toUid
    })
    this.getHomePage(toUid);
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function (options) {
    // this.getHomePage(undefined);
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