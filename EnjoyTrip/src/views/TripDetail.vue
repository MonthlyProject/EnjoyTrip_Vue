<template>
    <div>
        <h1 class="title">{{ content.title }}</h1>
        <b-img :src="content.firstImage"></b-img>
        <b-card bg-variant="light" text-variant="black" class="card">
            <b-card-text>
                {{content.overview}}
            </b-card-text>
        </b-card>
        <b-button @click="back()" class="btn">뒤로</b-button>
    </div>
</template>

<script>
import http from '@/util/http-common';
export default {
    data() {
        return {
            content: [],
        }
    },
    created() {
        let contentid = this.$route.params.contentid;
        http.get(`/vmap/detail/${contentid}`)
            .then(({ data }) => {
                console.log(data);
                this.content = data[0];
            })
    },
    methods: {
        back() {
            this.$router.push({ name: "map" });
        }
    }
}
</script>

<style scoped>
.card{
    margin: 10px;
    margin-left: 10%;
    margin-right: 10%;
    font-size: 20px;
    font-weight: 500;
}

.btn{
    width: 100px;
    height: 50px;
    font-size: 20px;
    margin-bottom: 10px;
}
</style>