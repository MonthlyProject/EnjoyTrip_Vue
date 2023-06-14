<template>
    <div>
      <b-button @click="DrawingFlag()" class="btn">{{ msg }}</b-button>
      <b-button @click="DeleteDrawing()" class="btn">경로 지우기</b-button>
      <div id="map">
        <add-star-modal
          ref="addStarModal"
          :modalShow="modalShow"
          :modalContent="modalContent"
        ></add-star-modal>
      </div>
    </div>
  </template>
  
  <script>
  import AddStarModal from "@/components/map/AddStarModal.vue";
  
  export default {
    name: "KakaoMap",
    data() {
      return {
        map: null,
        markers: [],
        markersId: [],
        markerscontent: [],
        mvforcus: null,
        modalShow: false,
        modalContent: null,
        //드래그
        drawingFlag: 0,
        moveLine: null,
        clickLine: null,
        distanceOverlay: null,
        dots: [],
        msg: "측정 시작",
      };
    },
    components: {
      AddStarModal,
    },
    props: {
      markerinfo: Array,
      movelocation: Object,
    },
    mounted() {
      if (window.kakao && window.kakao.maps) {
        this.loadMap();
      } else {
        this.loadScript();
      }
    },
    methods: {
      DrawingFlag() {
        if (!this.drawingFlag) {
          this.drawingFlag = 1;
          this.msg = "측정 중지";
        } else {
          this.drawingFlag = 0;
          this.msg = "측정 시작";
        }
      },
      DeleteDrawing() {
        if (this.drawingFlag == 2) {
          this.moveLine.setMap(null);
          this.moveLine = null;
          var path = this.clickLine.getPath();
          if (path.length > 1) {
            if (this.dots[this.dots.length - 1].distance) {
              this.dots[this.dots.length - 1].distance.setMap(null);
              this.dots[this.dots.length - 1].distance = null;
            }
            var distance = Math.round(this.clickLine.getLength()),
              content = this.getTimeHTML(distance);
            this.showDistance(content, path[path.length - 1]);
          }
        }
        if (this.drawingFlag != 0) {
          this.drawingFlag = 1;
        }
        this.deleteClickLine();
        this.deleteCircleDot();
        this.deleteDistance();
      },
      checkFormValidity() {
        const valid = this.$refs.form.checkValidity();
        this.nameState = valid;
        return valid;
      },
      resetModal() {
        this.name = "";
        this.nameState = null;
      },
      handleOk(bvModalEvent) {
        // Prevent modal from closing
        bvModalEvent.preventDefault();
        // Trigger submit handler
        this.handleSubmit();
      },
      handleSubmit() {
        // Exit when the form isn't valid
        if (!this.checkFormValidity()) {
          return;
        }
        // Push the name to submitted names
        this.submittedNames.push(this.name);
        // Hide the modal manually
        this.$nextTick(() => {
          this.$bvModal.hide("modal-prevent-closing");
        });
      },
      loadScript() {
        const script = document.createElement("script");
        script.src =
          "//dapi.kakao.com/v2/maps/sdk.js?appkey=kakaokey&autoload=false";
        script.onload = () => window.kakao.maps.load(this.loadMap);
        document.head.appendChild(script);
      },
      loadMap() {
        const container = document.getElementById("map");
        const options = {
          center: new window.kakao.maps.LatLng(33.450701, 126.570667),
          level: 3,
        };
        this.mvforcus = new window.kakao.maps.LatLng(33.450701, 126.570667);
        this.map = new window.kakao.maps.Map(container, options);
        window.kakao.maps.event.addListener(this.map, "click", (moveEvent) => {
          var clickPosition = moveEvent.latLng;
          if (this.drawingFlag == 1) {
            this.drawingFlag = 2;
            this.deleteClickLine();
            this.deleteDistance();
            this.deleteCircleDot();
            this.clickLine = new window.kakao.maps.Polyline({
              map: this.map,
              path: [clickPosition],
              strokeWeight: 3,
              strokeColor: "#db4040",
              strokeOpacity: 1,
              strokeStyle: "solid",
            });
            this.moveLine = new window.kakao.maps.Polyline({
              strokeWeight: 3,
              strokeColor: "#db4040",
              strokeOpacity: 0.5,
              strokeStyle: "solid",
            });
            this.displayCircleDot(clickPosition, 0);
          } else if (this.drawingFlag == 2) {
            var path = this.clickLine.getPath();
            path.push(clickPosition);
            this.clickLine.setPath(path);
            var distance = Math.round(this.clickLine.getLength());
            this.displayCircleDot(clickPosition, distance);
          }
        });
        window.kakao.maps.event.addListener(this.map, "mousemove", (mouseEvent) => {
          if (this.drawingFlag == 2 && this.clickLine != null) {
            var mousePosition = mouseEvent.latLng;
            var path = this.clickLine.getPath();
            var movepath = [path[path.length - 1], mousePosition];
            this.moveLine.setPath(movepath);
            this.moveLine.setMap(this.map);
            var distance = Math.round(this.clickLine.getLength() + this.moveLine.getLength()),
              content =
                '<div class="dotOverlay distanceInfo">총거리 <span class="number">' +
                distance +
                "</span>m</div>";
            this.showDistance(content, mousePosition);
          }
        });
        window.kakao.maps.event.addListener(this.map, "rightclick", () => {
          if (this.drawingFlag == 2) {
            this.moveLine.setMap(null);
            this.moveLine = null;
            var path = this.clickLine.getPath();
            if (path.length > 1) {
              if (this.dots[this.dots.length - 1].distance) {
                this.dots[this.dots.length - 1].distance.setMap(null);
                this.dots[this.dots.length - 1].distance = null;
              }
              var distance = Math.round(this.clickLine.getLength()),
                content = this.getTimeHTML(distance);
              this.showDistance(content, path[path.length - 1]);
            }
            this.drawingFlag = 1;
          }
        });
      },
      deleteClickLine() {
        if (this.clickLine) {
          this.clickLine.setMap(null);
          this.clickLine = null;
        }
      },
      showDistance(content, position) {
        if (this.distanceOverlay) {
          this.distanceOverlay.setPosition(position);
          this.distanceOverlay.setContent(content);
        } else {
          this.distanceOverlay = new window.kakao.maps.CustomOverlay({
            map: this.map,
            content: content,
            position: position,
            xAnchor: 0,
            yAnchor: 0,
            zIndex: 3,
          });
        }
      },
      deleteDistance() {
        if (this.distanceOverlay) {
          this.distanceOverlay.setMap(null);
          this.distanceOverlay = null;
        }
      },
      displayCircleDot(position, distance) {
        var circleOverlay = new window.kakao.maps.CustomOverlay({
          content: '<span class="dot"></span>',
          position: position,
          zIndex: 1,
        });
        circleOverlay.setMap(this.map);
        if (distance > 0) {
          var distanceOverlay = new window.kakao.maps.CustomOverlay({
            content:
              '<div class="dotOverlay">거리 <span class="number">' + distance + "</span>m</div>",
            position: position,
            yAnchor: 1,
            zIndex: 2,
          });
          distanceOverlay.setMap(this.map);
        }
        this.dots.push({ circle: circleOverlay, distance: distanceOverlay });
      },
      deleteCircleDot() {
        for (var i = 0; i < this.dots.length; i++) {
          if (this.dots[i].circle) {
            this.dots[i].circle.setMap(null);
          }
          if (this.dots[i].distance) {
            this.dots[i].distance.setMap(null);
          }
        }
        this.dots = [];
      },
      getTimeHTML(distance) {
        var walkTime = (distance / 67) | 0;
        var walkHour = "",
          walkMin = "";
  
        if (walkTime > 60) {
          walkHour = '<span class="number">' + Math.floor(walkTime / 60) + "</span>시간";
        }
        walkMin = '<span class="number">' + (walkTime % 60) + "</span>분";
        var bycicleTime = (distance / 227) | 0;
        var bycicleHour = "",
          bycicleMin = "";
  
        if (bycicleTime > 60) {
          bycicleHour = '<span class="number">' + Math.floor(bycicleTime / 60) + "</span>시간 ";
        }
        bycicleMin = '<span class="number">' + (bycicleTime % 60) + "</span>분";
  
        var content = '<ul class="dotOverlay distanceInfo">';
        content += "    <li>";
        content +=
          '        <span class="label">총거리</span><span class="number">' + distance + "</span>m";
        content += "    </li>";
        content += "    <li>";
        content += '        <span class="label">도보</span>' + walkHour + walkMin;
        content += "    </li>";
        content += "    <li>";
        content += '        <span class="label">자전거</span>' + bycicleHour + bycicleMin;
        content += "    </li>";
        content += "</ul>";
  
        return content;
      },
      getmarkerimage(contentTypeId) {
        if (contentTypeId == "12") {
          return "https://ifh.cc/g/Sa7vl8.png";
        } else if (contentTypeId == "14") {
          return "https://ifh.cc/g/OwRvXM.png";
        } else if (contentTypeId == "15") {
          return "https://ifh.cc/g/VN0CXp.png";
        } else if (contentTypeId == "25") {
          return "https://ifh.cc/g/NbToXZ.png";
        } else if (contentTypeId == "28") {
          return "https://ifh.cc/g/LpMmLp.png";
        } else if (contentTypeId == "32") {
          return "https://ifh.cc/g/BFY95t.png";
        } else if (contentTypeId == "38") {
          return "https://ifh.cc/g/spMNRp.png";
        } else if (contentTypeId == "39") {
          return "https://ifh.cc/g/SBnqrS.png";
        }
      },
      setmarker(mapmap) {
        for (var i = 0; i < this.markers.length; i++) {
          this.markers[i].setMap(mapmap);
          window.kakao.maps.event.addListener(
            this.markers[i],
            "click",
            this.makeOverListener(this.map, this.markers[i], this.markerscontent[i])
          );
          // window.kakao.maps.event.addListener(this.markers[i], 'click', this.makeOverListener(i));
          // window.kakao.maps.event.addListener(this.markers[i], 'mouseout', this.makeOutListener(this.markerscontent[i]));
          // window.kakao.maps.event.addListener(this.markers[i], 'click', this.makeClickListener(this.markersId[i]));
        }
        this.map.setCenter(this.mvforcus); //걍 이동
        //map.panTo(location);
      },
      makeOverListener(map, marker, infowindow) {
        return function () {
          infowindow.open(map, marker);
        };
      },
      hideModal() {
        this.$refs["modal"].hide();
      },
    },
    watch: {
      markerinfo() {
        this.setmarker(null);
        this.markers = [];
        this.markersId = [];
        this.markerscontent = [];
        for (let marker of this.markerinfo) {
          this.mvforcus = new window.kakao.maps.LatLng(`${marker.latitude}`, `${marker.longitude}`);
          var markerimgsize = new window.kakao.maps.Size(32, 36),
            imageOption = {
              offset: new window.kakao.maps.Point(13, 34),
            };
          //마크 위치와 이미지 세팅
          var mark = new window.kakao.maps.Marker({
            position: this.mvforcus,
            image: new window.kakao.maps.MarkerImage(
              this.getmarkerimage(marker.contentTypeId),
              markerimgsize,
              imageOption
            ),
          });
  
          if (marker.firstimage === null) {
            //사진이 없을 때 대체할 사진 넣기
            marker.firstimage = "https://ifh.cc/g/4t6HWw.png";
          }
  
          // let content = `<div style="width:400px;">
          //                 <form id="form-register" method="POST" action="">
          //                     <input type="hidden" name="action" value="addStar">
          //                     <input type="hidden" name="contentId" value="${marker.contentId}">
          //                     <a style="display: block; padding: 6px;background-color: deepskyblue; font-size: 20px; font-weight: bold; color: white;display: block; text-align: center;" name="title" value="${marker.title}">${marker.title}</a>
          //                     <div style="display: flex; justify-content: center; align-items: center;">
          //                         <img src="${marker.firstImage}" style="width:200px;" name="image" value="${marker.firstInage}">
          //                         <a style="margin: 20px;" name="addr" value="${marker.addr1} ${marker.addr2}">${marker.addr1} ${marker.addr2}</a>
          //                     </div>
          //                 </form>
          //             </div>`;
  
          let iwContent = document.createElement("div");
          iwContent.style = `width:400px`;
  
          let iwForm = document.createElement("form");
          iwForm.id = `form-register`;
          iwForm.method = `POST`;
  
          let iwInput1 = document.createElement("input");
          iwInput1.type = `hidden`;
          iwInput1.name = `action`;
          iwInput1.value = `addStar`;
  
          let iwInput2 = document.createElement("input");
          iwInput2.type = `hidden`;
          iwInput2.name = `contentId`;
          iwInput2.value = `${marker.contentId}`;
  
          let iwA1 = document.createElement("a");
          iwA1.style = `display: block; padding: 6px; background-color: deepskyblue; font-size: 20px; font-weight: bold; color: white;display: block; text-align: center;" name="title" value="${marker.title}">${marker.title}`;
          iwA1.innerHTML = `${marker.title}`;
  
          let iwDiv = document.createElement("div");
          iwDiv.style = `display: flex; justify-content: center; align-items: center;`;
          iwDiv.onclick = () => {
            this.$router.push({ name: "tripdetail", params: { contentid: `${marker.contentId}` } });
          };
  
          let iwImg = document.createElement("img");
          iwImg.src = `${marker.firstImage}`;
          iwImg.style = `width:200px;`;
          iwImg.name = `image`;
          iwImg.value = `${marker.firstInage}`;
  
          let iwA2 = document.createElement("a");
          iwA2.style = `margin: 20px`;
          iwA2.name = `addr`;
          iwA2.value = `${marker.addr1} ${marker.addr2}`;
          iwA2.innerHTML = `${marker.addr1} ${marker.addr2}`;
  
          let iwButton = document.createElement("a");
          iwButton.onclick = () => {
            this.modalContent = marker;
            this.$refs.addStarModal.$refs.addStarModal.show();
          };
          iwButton.innerHTML = `추가`;
  
          iwDiv.append(iwImg, iwA2);
  
          iwForm.append(iwButton, iwInput1, iwInput2, iwA1, iwDiv);
  
          iwContent.append(iwForm);
  
          var infowindow = new window.kakao.maps.InfoWindow({
            //태그를 건드렸을 때 생성할 창
            position: this.mvforcus,
            content: iwContent,
            removable: true,
          });
          this.markersId.push(marker.contentId);
          this.markers.push(mark); //배열에 마크 넣기
          this.markerscontent.push(infowindow);
        }
        this.setmarker(this.map); //배열에 든 마크 찍기
      },
      movelocation() {
        if (this.movelocation !== null) {
          this.map.setCenter(
            new window.kakao.maps.LatLng(
              `${this.movelocation.latitude}`,
              `${this.movelocation.longitude}`
            )
          ); //걍 이동
          for (let i = 0; i < this.markers.length; i++) {
            if (this.movelocation.contentId === this.markersId[i]) {
              this.markerscontent[i].open(this.map, this.markers[i]);
            }
          }
        }
      },
    },
  };
  </script>
  
  <style>
  #map {
    width: 100%;
    height: 670px;
  }
  .dot {
    overflow: hidden;
    float: left;
    width: 12px;
    height: 12px;
    background: url("https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/mini_circle.png");
  }
  .dotOverlay {
    position: relative;
    bottom: 10px;
    border-radius: 6px;
    border: 1px solid #ccc;
    border-bottom: 2px solid #ddd;
    float: left;
    font-size: 12px;
    padding: 5px;
    background: #fff;
  }
  .dotOverlay:nth-of-type(n) {
    border: 0;
    box-shadow: 0px 1px 2px #888;
  }
  .number {
    font-weight: bold;
    color: #ee6152;
  }
  .dotOverlay:after {
    content: "";
    position: absolute;
    margin-left: -6px;
    left: 50%;
    bottom: -8px;
    width: 11px;
    height: 8px;
    background: url("https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white_small.png");
  }
  .distanceInfo {
    position: relative;
    top: 5px;
    left: 5px;
    list-style: none;
    margin: 0;
  }
  .distanceInfo .label {
    display: inline-block;
    width: 50px;
  }
  .distanceInfo:after {
    content: none;
  }
  </style>
  
