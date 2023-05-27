<template>
    <b-modal id="login-modal" ref="findpwd" title="비밀번호찾기" @ok="find">
        <form ref="form" @submit.stop.prevent="handleSubmit">
            <!-- 아이디 -->
            <b-form-group label="아이디" label-for="user-id-input">
                <b-form-input id="user-id-input" v-model="userId"
                    @keyup.enter="confirm"></b-form-input>
            </b-form-group>

            <!-- 이메일 -->
            <b-form-group label="이메일" label-for="email-id-input">
                <b-row>
                    <b-col cols="" class="pr-0" style="flex-grow: 2 !important">
                        <b-form-input class="mb-2 mr-sm-2 mb-sm-0" id="email-id-input" v-model="emailId"></b-form-input>
                    </b-col>
                    <b-col cols="auto" style="padding: 0 !important; margin: 0px !important">
                        <span class="text-left input-group-text">@</span>
                    </b-col>

                    <!-- 이메일 도메인 -->
                    <b-col cols="" style="width: 100% !important; flex-grow: 3 !important; padding-left: 0 !important">
                        <b-form-select prepend="@" id="email-domain-input" :options="options"
                            v-model="emailDomain"></b-form-select>
                    </b-col>
                </b-row>
            </b-form-group>
        </form>
    </b-modal>
</template>

<script>
import http from '@/util/http-common';

export default {
    data() {
        return {
            userId: '',
            emailId: '',
            emailDomain: null,
            options: [
                { text: "선택하세요", value: null },
                { text: "naver.com", value: "naver.com" },
                { text: "gmail.com", value: "gmail.com" },
                { text: "kakao.com", value: "kakao.com" },
                { text: "ssafy.com", value: "ssafy.com" },
            ],

        }
    },
    computed: {
    },
    methods: {
        find() {
            if (this.userId === '') {
                alert('아이디를 입력하세요');
            } else if (this.emailId === '') {
                alert('이메일 아이디를 입력하세요');
            } else if (this.emailDomain === null) {
                alert('이메일 도메인을 선택하세요');
            } else {
                http.get(`/vuser/${this.userId}/${this.emailId}/${this.emailDomain}`)
                    .then(({ data }) => {
                        alert("비밀번호 : " + data);
                    })
            }
        }
    },
};
</script>

<style scoped></style>
