<template>
  <b-modal
    id="register-modal"
    ref="register"
    title="회원가입"
    @show="resetModal"
    @hidden="resetModal"
    @ok="submit"
  >
    <form ref="form" @submit.stop.prevent="handleSubmit">
      <!-- 이름 -->

      <b-form-group
        label="이름"
        label-for="name-input"
        :invalid-feedback="nameWarn"
        :state="nameState"
        readonly
      >
        <!-- <b-form-input id="name-input" v-model="name" :state="nameState" required></b-form-input> -->
        <b-form-input
          id="name-input"
          v-model="inputParam.name"
          @blur="userNameCheck"
          :state="nameState"
          required
        ></b-form-input>
      </b-form-group>

      <!-- 아이디 -->
      <b-form-group
        label="아이디"
        label-for="user-id-input"
        :invalid-feedback="uidWarn"
        :state="userIdState"
      >
        <b-row>
          <b-col cols="" style="flex-grow: 1 !important">
            <b-form-input
              id="user-id-input"
              v-model="inputParam.userId"
              @blur="userIdCheck"
              :state="userIdState"
              required
            ></b-form-input>
          </b-col>

          <!-- 아이디 확인 버튼 -->
          <b-col cols="auto" class="" style="padding: 0 !important; margin-right: 16px !important">
            <b-button @click="idDupCheck">확인</b-button>
          </b-col>
        </b-row>
      </b-form-group>

      <!-- 비밀번호 -->
      <b-form-group
        label="비밀번호"
        label-for="user-password-input"
        :invalid-feedback="pwdWarn"
        :state="passwordState"
      >
        <b-form-input
          id="user-password-input"
          type="password"
          v-model="inputParam.password"
          @blur="userPwdCheck"
          :state="passwordState"
          required
        ></b-form-input>
      </b-form-group>

      <!-- 비밀번호 확인 -->
      <b-form-group
        label="비밀번호 확인"
        label-for="user-password-check-input"
        :invalid-feedback="pwdCheckWarn"
        :state="passwordCheckState"
      >
        <b-form-input
          id="user-password-check-input"
          type="password"
          v-model="inputParam.passwordCheck"
          @blur="pwdCheck"
          :state="passwordCheckState"
          required
        ></b-form-input>
      </b-form-group>

      <!-- 이메일 -->
      <!-- 이메일 아이디 -->
      <b-form-group
        label="이메일"
        label-for="email-id-input"
        :invalid-feedback="emailIdWarn"
        :state="emailIdState"
      >
        <b-row>
          <b-col cols="" class="pr-0" style="flex-grow: 2 !important">
            <b-form-input
              class="mb-2 mr-sm-2 mb-sm-0"
              id="email-id-input"
              v-model="inputParam.emailId"
              @blur="emailIdCheck"
              :state="emailIdState"
              :invalid-feedback="emailIdWarn"
              required
            ></b-form-input>
          </b-col>
          <b-col cols="auto" style="padding: 0 !important; margin: 0px !important">
            <span class="text-left input-group-text">@</span>
          </b-col>

          <!-- 이메일 도메인 -->
          <b-col
            cols=""
            style="width: 100% !important; flex-grow: 3 !important; padding-left: 0 !important"
          >
            <b-form-select
              prepend="@"
              id="email-domain-input"
              :options="options"
              v-model="inputParam.emailDomain"
              :state="emailDomainState"
            ></b-form-select>
          </b-col>
        </b-row>
      </b-form-group>
    </form>
  </b-modal>
</template>

<script>
import http from "@/util/http-common.js";
import { mapActions } from "vuex";

const memberStore = "memberStore";

export default {
  name: "RegisterModalVue",
  data() {
    return {
      inputParam: {
        name: "",
        userId: "",
        password: "",
        passwordCheck: "",
        emailId: "",
        emailDomain: null,
      },

      // 이름 입력 체크
      nameState: false,
      nameIsNull: true,
      nameSpaceCheck: false,

      // 아이디 입력 체크
      userIdState: false,
      idIsNull: true,
      idSpecialCheck: false,
      idSpaceCheck: false,
      idDupConfirm: false,
      idDupState: null,

      // 비밀번호 입력 체크
      passwordState: false,
      pwdIsNull: true,
      pwdSpecialCheck: false,
      pwdSpaceCheck: false,

      // 비밀번호 확인 입력 체크
      passwordCheckState: false,
      pwdCheckIsNull: true,

      // 이메일 입력 체크
      emailIdState: null,
      emailIdIsNull: true,
      emailIdSpaceCheck: false,

      // 이메일 도메인 선택 옵션
      options: [
        { text: "선택하세요", value: null },
        { text: "naver.com", value: "naver.com" },
        { text: "gmail.com", value: "gmail.com" },
        { text: "kakao.com", value: "kakao.com" },
        { text: "ssafy.com", value: "ssafy.com" },
      ],

      // 정규 표현식
      IDspecialCharacters: /[ \{\}\[\]\/?.,;:|\)*~`!^\-_+|<>@\#$%&\'\"\\\(\=]/gi,
      PWDspecialCharacters: /[ \{\}\[\]\/.,;:|\)~`\-_+|<>%&\'\"\\\(\=]/gi,
      spaceCharacters: /[\s]/,
      charKOR: /[ㄱ-ㅎ|ㅏ-ㅣ|가-힣]/,
    };
  },
  computed: {
    // 경고 표시
    // 이메일 도메인
    emailDomainState: {
      set: function () {},
      get: function () {
        return this.inputParam.emailDomain !== null;
      },
    },
    // 사용자 이름 경고
    nameWarn: {
      set: function () {},
      get: function () {
        if (this.nameSpaceCheck) {
          return "이름에 공백이 있습니다.";
        } else if (this.nameState === false) {
          return "이름은 필수 입력입니다";
        }
      },
    },
    // 사용자 아이디 입력 경고
    uidWarn: {
      set: function () {},
      get: function () {
        if (this.idIsNull) {
          return "아이디는 필수 입력입니다.";
        } else if (this.idSpecialCheck) {
          return "아이디는 영문, 숫자로만 이루어져야합니다.";
        } else if (this.idSpaceCheck) {
          return "아이디에 공백이 있습니다.";
        } else if (this.idDupState) {
          if (!this.idDupConfirm) {
            return "아이디 중복 확인을 해주세요.";
          } else {
            return "중복된 아이디입니다.";
          }
        }
      },
    },
    // 사용자 비밀번호 입력 경고
    pwdWarn: {
      set: function () {},
      get: function () {
        if (this.pwdIsNull) {
          return "비밀번호는 필수 입력입니다";
        } else if (this.pwdSpecialCheck) {
          return "비밀번호는 영문 대,소문자, ?!@#$^*만 입력이 가능합니다.";
        } else if (this.pwdSpaceCheck) {
          return "비밀번호에 공백이 있습니다.";
        }
      },
    },
    // 사용자 비밀번호 확인 입력 경고
    pwdCheckWarn: {
      set: function () {},
      get: function () {
        if (this.pwdCheckIsNull) {
          return "비밀번호는 필수 입력입니다";
        } else if (this.passwordCheckState === false) {
          return "비밀번호가 일치하지 않습니다";
        }
      },
    },
    // 사용자 이메일 입력 경고
    emailIdWarn: {
      set: function () {},
      get: function () {
        if (this.emailIdIsNull) {
          return "이메일은 필수 입력입니다.";
        } else if (this.emailIdSpaceCheck) {
          return "이메일에 공백이 있습니다.";
        }
      },
    },
  },
  methods: {
    // 회원가입 버튼 시 validity 검증
    checkFormValidity() {
      return (
        this.nameState &&
        this.userIdState &&
        this.passwordState &&
        this.passwordCheckState &&
        this.emailIdState &&
        this.emailDomainState
      );
    },
    // 사용자 이름 입력 검증
    userNameCheck() {
      this.nameState = false;
      this.nameIsNull = false;
      this.nameSpaceCheck = false;
      if (
        // 이름 입력이 되어있지않았을 때
        this.inputParam.name === null ||
        this.inputParam.name === undefined ||
        this.inputParam.name.replace(/^\s+|\s+$/g, "") === ""
      ) {
        this.nameIsNull = true;
      } else if (this.spaceCharacters.test(this.inputParam.name)) {
        // 이름에 공백이 있을 때
        this.nameSpaceCheck = true;
      } else {
        // 정상입력
        this.nameState = true;
      }
    },
    // 사용자 아이디 입력 검증
    userIdCheck() {
      this.userIdState = false;
      this.idIsNull = false;
      this.idSpecialCheck = false;
      this.idSpaceCheck = false;
      this.idDupConfirm = false;
      this.idDupState = null;
      if (
        // 아이디 입력받지 않았을 때
        this.inputParam.userId === null ||
        this.inputParam.userId === undefined ||
        this.inputParam.userId.replace(/^\s+|\s+$/g, "") === ""
      ) {
        this.idIsNull = true;
      } else if (this.spaceCharacters.test(this.inputParam.userId) === true) {
        // 아이디에 공백이 포함되어있을 때

        this.idSpaceCheck = true;
      } else if (
        // 아이디에 한글이나 특수문자가 포함되어있을 때
        this.charKOR.test(this.inputParam.userId) === true ||
        this.IDspecialCharacters.test(this.inputParam.userId) === true
      ) {
        this.idSpecialCheck = true;
      } else if (this.idDupConfirm == false) {
        this.idDupState = true;
      } else {
        // 정상 아이디
        this.userIdState = true;
      }
      console.log(this.userIdState);
    },
    // 사용자 아이디 중복 체크
    idDupCheck() {
      console.log("idDupCheck");
      http
        .post(`/vuser/idCheck`, {
          userId: this.userId,
        })
        .then(({ data }) => {
          if (data.message === "success") {
            // 중복이 아니면 false
            console.log("dup check success");
            this.userIdState = true;
            this.idDupConfirm = true;
            this.idDupState = false;
          } else {
            // 중복이면  true
            console.log("dup check fail");
            this.userIdCheck = false;
            this.idDupConfirm = false;
            this.idDupState = true;
          }
        });
    },
    // 사용자 비밀번호 입력 검증
    userPwdCheck() {
      this.passwordState = false;
      this.pwdIsNull = false;
      this.pwdSpecialCheck = false;
      this.pwdSpaceCheck = false;
      if (
        // 비밀번호 입력받지 않았을 때
        this.inputParam.password == null ||
        this.inputParam.password == undefined ||
        this.inputParam.password.replace(/^\s+|\s+$/g, "") === ""
      ) {
        this.pwdIsNull = true;
      } else if (
        // 비밀번호에 한글이나 특수문자가 있을 때
        this.PWDspecialCharacters.test(this.inputParam.password) ||
        this.charKOR.test(this.inputParam.password)
      ) {
        this.pwdSpecialCheck = true;
      } else if (this.spaceCharacters.test(this.inputParam.password)) {
        // 비밀번호에 공백이 있을 때
        this.pwdSpaceCheck = true;
      } else {
        this.passwordState = true;
      }
    },
    // 사용자 비밀번호 확인 입력 검증
    pwdCheck() {
      this.pwdCheckIsNull = false;
      this.passwordCheckState = false;
      if (
        this.inputParam.passwordCheck == null ||
        this.inputParam.passwordCheck == undefined ||
        this.inputParam.passwordCheck.replace(/^\s+|\s+$/g, "") === ""
      ) {
        this.pwdCheckIsNull = true;
      } else if (this.inputParam.password === this.inputParam.passwordCheck) {
        this.passwordCheckState = true;
      }
    },
    // 사용자 이메일아이디 입력 검증
    emailIdCheck() {
      this.emailIdState = false;
      this.emailIdIsNull = false;
      this.emailIdSpaceCheck = false;
      if (
        this.inputParam.emailId == null ||
        this.inputParam.emailId == undefined ||
        this.inputParam.emailId.replace(/^\s+|\s+$/g, "") === ""
      ) {
        this.emailIdIsNull = true;
      } else if (this.spaceCharacters.test(this.inputParam.emailId)) {
        this.emailIdSpaceCheck = true;
      } else {
        this.emailIdState = true;
      }
    },
    // 모달 껐다가 켰을 때 입력창 초기화
    resetModal() {
      this.inputParam.name = "";
      this.nameState = null;
      this.inputParam.userId = "";
      this.userIdState = null;
      this.inputParam.password = "";
      this.passwordState = null;
      this.inputParam.passwordCheck = "";
      this.passwordCheckState = null;
      this.inputParam.emailId = "";
      this.emailIdState = null;
      this.inputParam.emailDomain = null;
      this.emailDomainState = null;
      this.idDupState = null;
    },
    // 회원가입 입력 버튼 눌렀을 때
    submit(bvModalEvent) {
      // Prevent modal from closing
      bvModalEvent.preventDefault();
      // Trigger submit handler
      this.handleSubmit();
    },
    handleSubmit() {
      // 회원가입 양식이 맞지 않을 때
      if (!this.checkFormValidity()) {
        //console.log(this.checkFormValidity());
        return;
      }

      // 정상 입력했을 때
      http
        .post(`/vuser/join`, {
          userId: this.inputParam.userId,
          userPwd: this.inputParam.password,
          userName: this.inputParam.name,
          emailId: this.inputParam.emailId,
          emailDomain: this.inputParam.emailDomain,
        })
        .then(({ data }) => {
          let msg = "회원가입 처리시 문제가 발생했습니다.";
          // console.log(data.message);
          if (data.message === "success") {
            msg = "회원가입이 완료되었습니다.";
          }
          alert(msg);
          // 모달 닫기
          this.$bvModal.hide("register-modal");
        });
    },
  },
};
</script>

<style scoped></style>
