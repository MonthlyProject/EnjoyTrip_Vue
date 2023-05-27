<template>
  <div>
    <b-container fluid="fluid" class="p-4 background">
      <b-row cols="1" cols-md="1" cols-lg="2" align-v="center">
        <b-col>
          <h1 class="main-title">Welcome to Trip Scanner</h1>
          <p class="main-content">Trip Scanner is ready to be your best travel mate !</p>
          <div style="display: flex; align-items: center">
            <button class="start-btn" href="#about">Get Started</button>
            <a class="watch-video" href="https://youtu.be/CcCjvSZQ-Xc">
              <b-icon
                icon="play-circle"
                font-scale="1.5"
                style="margin-right: 10px; color: rgba(255, 255, 255, 0.4)"
              ></b-icon
              >Watch Video
            </a>
          </div>
        </b-col>
        <b-col>
          <b-img
            class="main-img"
            fluid="fluid"
            src="https://i0.wp.com/www.korvia.com/wp-content/uploads/2015/11/image-header-live-in-jeju.jpg?fit=960%2C640&ssl=1"
            alt="Image 1"
          ></b-img>
        </b-col>
      </b-row>
    </b-container>
    <b-container fluid="fluid" class="side-blank">
      <b-row cols="1" cols-sm="1" cols-md="2" cols-lg="3" cols-xl="5" align-v="center">
        <b-col>
          <button class="main-btn" @click="goMap">
            <b-icon class="icon" icon="geo-alt" font-scale="3"></b-icon>
            <br />
            <a class="center">관광지 조회</a>
          </button>
        </b-col>
        <b-col>
          <button class="main-btn" @click="goInform">
            <b-icon class="icon" icon="patch-check-fill" font-scale="3"></b-icon>
            <br />
            <a class="center">공지사항</a>
          </button>
        </b-col>
        <b-col>
          <button class="main-btn" @click="goBoard">
            <b-icon class="icon" icon="card-heading" font-scale="3"></b-icon>
            <br />
            <a class="center">게시판</a>
          </button>
        </b-col>
        <b-col>
          <button class="main-btn" @click="goStar">
            <b-icon class="icon" icon="star-fill" font-scale="3"></b-icon>
            <br />
            <a class="center">여행 계획</a>
          </button>
        </b-col>
        
        <b-col>
                    <button class="main-btn" @click="goChart">
                        <b-icon class="icon" icon="bar-chart-line" font-scale="3"></b-icon>
                        <br>
                        <a class="center">검색 차트</a>
                    </button>
                </b-col>
      </b-row>
    </b-container>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";

const memberStore = "memberStore";
const mapStore = "mapStore";

export default {
  name: "HomeView",
  components: {},
  methods: {
    goMap() {
      this.$router.push({ name: "map" });
    },
    goBoard() {
      this.$router.push({ name: "board" });
    },
    goStar() {
      if (this.isLogin) {
        this.$router.push({ name: "star" });
      } else {
        alert("로그인해주세요");
      }
    },
    goInform() {
      this.$router.push({ name: "inform" });
    },
    goChart() {
        this.$router.push({name: "chart"});
    },
    ...mapActions(mapStore, ["getChartData"]),
  },
  computed: {
    ...mapState(memberStore, ["isLogin"]),
    ...mapState(mapStore, ["chartData"]),
  },
  created() {
    this.getChartData("sido");
    console.log(this.chartData);
  }
};
</script>

<style scoped="scoped">
.main-btn {
  background-color: #008374;
  border-style: none;
  box-shadow: 0 -70px 50px 10px rgba(0, 0, 0, 0.08);
  height: 250px;
  width: 100%;
  border-radius: 7px;
  margin: 5%;
  position: relative;
}

.main-btn:hover {
  background-color: #009786;
}

.main-img {
  padding: 10%;
  width: 800px;
}

.side-blank {
  padding-right: 5%;
  padding-left: 5%;
  background: linear-gradient(0deg, rgba(255, 255, 255, 0) 50%, #008374 0%);
}

.icon {
  color: rgba(255, 255, 255, 0.4);
  position: absolute;
  top: 30%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.center {
  text-decoration: none;
  color: white;
  font-size: 30px;
  font-weight: 600;
  position: absolute;
  top: 60%;
  left: 50%;
  transform: translate(-50%, -50%);
  white-space: nowrap;
}

.main-title {
  color: white;
  font-size: 50px;
  font-weight: 700;
  margin-left: 10%;
  display: flex;
}

.main-content {
  color: rgba(255, 255, 255, 0.4);
  font-size: 20px;
  font-weight: 500;
  display: flex;
  margin-left: 10%;
}

.start-btn {
  color: white;
  background-color: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.2);
  border-radius: 30px;
  width: 170px;
  height: 55px;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-left: 10%;
}

.start-btn:hover {
  background-color: rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.658);
}

.watch-video {
  color: white;
  font-size: 20px;
  margin-left: 30px;
  display: flex;
  align-items: center;
  text-decoration: none;
}

.watch-video:hover {
  color: rgba(255, 255, 255, 0.527);
}
</style>
