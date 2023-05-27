<template>
    <div class="side-blank">
        <b-list-group>
            <!-- 클릭하면 클릭한 목록 밑에 작은 리스트를 만들기 -->
            <div v-if="groups != []">
                <div v-for="group in groups" :key="group">
                    <b-list-group-item @click="showList(group)">{{ group }}<b-icon class="icon" icon="trash-fill" font-scale="1" @click="deleteGroup(group)"></b-icon></b-list-group-item>
                    <div v-if="clickgroup===group">
                        <b-list-group-item v-for="trip in triplist" :key="trip.contentName" class="child" @click="moveMap(trip)">{{ trip.title }}<b-icon class="icon" icon="trash-fill" font-scale="1" @click="deleteTrip(trip.contentId)"></b-icon></b-list-group-item>
                    </div>
                </div>
            </div>
            <div v-else>즐겨찾기가 존재하지 않습니다.</div>
        </b-list-group>
    </div>
</template>

<script>
import http from '@/util/http-common';
import { mapState } from "vuex";

const memberStore = "memberStore";

export default {
    data() {
        return {
            groups: [],
            clickgroup: null,
            triplist: null,
            donot: true,
            nowgroup: null,
        }
    },
    created() {
        http.get(`/vmap/star/group/${this.userInfo.userId}`)
            .then(({ data }) => {
                this.groups = data;
            })
    },
    methods: {
        showList(group) {
            if (this.clickgroup === group) {
                this.clickgroup = null;
            } else {
                this.clickgroup = group;
            }
            if (this.donot) {
                http.post(`/vmap/getstar`, {
                    userId: this.userInfo.userId,
                    title: this.clickgroup,
                })
                    .then(({ data }) => {
                        this.triplist = data;
                        this.$emit("setmarkerinfo", this.triplist);
                    })
            }
        },
        deleteTrip(contentId) {
            http.delete(`/vmap/star/${this.userInfo.userId}/${this.clickgroup}/${contentId}`)
                .then(({ data }) => {
                    console.log(data);
                    http.post(`/vmap/getstar`, {
                        userId: this.userInfo.userId,
                        title: this.clickgroup,
                    })
                    .then(({ data }) => {
                        this.triplist = data;
                        this.$emit("setmarkerinfo", this.triplist);
                    })
                })
        },
        deleteGroup(group) {
            this.nowgroup = group;
            this.donot = false;
            http.delete(`/vmap/star/${this.userInfo.userId}/${this.nowgroup}`)
                .then(({ data }) => {
                    console.log(data);
                    http.get(`/vmap/star/group/${this.userInfo.userId}`)
                        .then(({ data }) => {
                            this.groups = data;
                            this.donot = true;
                        })
                })
        },
        moveMap(trip) {
            console.log(trip);
            this.$emit("moveMap", trip);
        }
    },
    computed: {
        ...mapState(memberStore, ["userInfo"]),
    }
}
</script>

<style scoped>
.child{
    margin-right: 10px;
    margin-left: 10px
}

.icon{
    float: right;
}
</style>