require(['jquery', 'vue', 'utils', 'weui', 'messager'], function ($, Vue, utils, weui, messager) {
    new Vue({
        el: '#content',
        data: {
            member: {},
            preview: null
        },
        methods: {
            getHeadPhoto: function () {
                if(this.preview) {
                    return this.preview;
                } else if(this.member.headPhoto) {
                    return utils.patchUrl('/attachment/download/' + this.member.headPhoto.id);
                } else {
                    return window._appConf.ctx + '/static/image/default_user.jpg';
                }
            },
            changeHeadPhoto: function () {
                $(this.$refs['headPhotoInput']).trigger('click');
            },
            chosenFile:function () {
                messager.loading({
                    message: '图片上传中'
                });
                var self = this;
                var reader = new FileReader();
                var file = event.srcElement.files[0];
                reader.onload = function(e) {
                    //获取图片dom
                    //图片路径设置为读取的图片
                    self.preview = e.target.result;
                };
                reader.readAsDataURL(file);
                $.ajax({
                    url: utils.patchUrl('/attachment/upload'),
                    contentType: false,
                    processData: false,
                    type: 'POST',
                    data: new FormData(this.$refs['uploadForm']),
                    success: function(datas) {
                        messager.bubble('上传完毕');
                        self.member.headPhoto = datas[0];
                        console.log(self.member);
                        messager.loading('close');
                    }
                });
            },
            save: function () {
                $.ajax({
                    url: utils.patchUrl('/api/member'),
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify(this.member),
                    success: function () {
                        messager.bubble('保存成功', 'success');
                    }
                })
            },
            logout: function () {
                window.localStorage.accessToken = null;
                window.localStorage.refreshToken = null;
                window.localStorage.expiration = null;
                window.location.href = utils.patchUrlPrefixUrl('/');
            }
        },
        mounted: function () {
            var self = this;
            utils.getLoginMember(function (member) {
                self.member = member;
            }, true);
        }
    });
});