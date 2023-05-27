<template>
    <div class="pt-4 main-container">
        <b-row align-h="center" class="mb-4">
            <h2 class="font-weight-bold">게시판</h2>
        </b-row>
        <div class="pt-3">
            <!-- 글쓰기 버튼, 검색 영역-->
            <b-row>
                <!-- 검색 옵션 -->
                <b-col cols="auto" class="p-3">
                    <b-form-group id="search-type" label-for="input-3">
                        <b-form-select
                        id="search"
                        v-model="form.type"
                        :options="options"
                        required
                        ></b-form-select>
                    </b-form-group>
                </b-col>
                <!-- 검색어 입력-->
                <b-col cols="auto" class="p-3">
                    <b-form-input placeholder="검색어 입력" v-model="form.word"></b-form-input>
                </b-col>
                <!-- 검색 버튼-->
                <b-col cols="auto" class="p-3">
                    <b-button type="submit" @click="searchArticle" variant="outline-primary">검색</b-button>
                </b-col>
            </b-row>

            <!-- 게시글 영역 -->
            <b-row>
                <b-col>
                    <b-table striped hover :items="articles" :fields="fields" :per-page="perPage" :current-page="currentPage">
                        <template #cell(delete)="data">
                            <b-button @click="deleteUser(data.item.userId)">삭제</b-button>
                        </template>
                        <template #cell(userEmail)="data">
                            {{ data.item.emailId }}@{{ data.item.emailDomain }}
                        </template>
                    </b-table>
                </b-col>
            </b-row>
            
            <b-pagination
                v-model="currentPage"
                :total-rows="rows"
                :per-page="perPage"
                aria-controls="my-table"
                align="center"
            ></b-pagination>
        </div>
    </div>
</template>

<script>
import http from '@/util/http-common.js';

export default {
    name: 'BoardList',
    data() {
        return {
            currentPage: 1,
            perPage: 20,
            form: {
                type: null,
                word: null,
            },
            options: [{text: '검색조건', value: null}, {text: '아이디', value: 'userId'}, {text: '이름', value: 'userName'}, {text: '이메일', value: 'userEmail'}],
            articles: [],
            fields: [
                { key: "userId", label: "아이디", tdClass: "tdId" },
                { key: "userName", label: "이름", tdClass: "tdName" },
                { key: "userEmail", label: "이메일", tdClass: "tdName" },
                { key: "delete", label: "삭제", tdClass: "tdDelete" },
            ],
        };
    },
    created() {
        http.get(`/vadmin`).then(({data}) => {
            this.articles = data;
        });
    },
    methods: {
        searchArticle(){
            // console.log(this.form.type);
            // console.log(this.form.word);
            http.get(`/vadmin/${this.form.type}/${this.form.word}`).then(({ data }) => {
                console.log(data);
                this.articles = data;
                // console.log(data);
            });
            //this.$router.push({name:"boardlist"})
        },
        pageClick: function (button, page) {
            this.currentPage = page;
            this.getNoticelistByPage(page);
        },
        getNoticelistByPage: function (page) {
            var limit = 20;
            var offset = (page - 1) * linit;

            this.loading = true;
            console.log(offset);
        },
        deleteUser(userId) {
            console.log(userId);
            http.post(`/vadmin/delete/${userId}`).then(({data}) => {
                this.articles = data;
            });
        }
    },
    computed: {
        rows() {
            return this.articles.length;
        }
    }
};
</script>

<style scoped>
.main-container {
    padding-left:  15%;
    padding-right: 15%; 
}

.tdClass {
    width: 50px;
    text-align: center;
}
.tdSubject {
    width: 300px;
    text-align: left;
}
</style>