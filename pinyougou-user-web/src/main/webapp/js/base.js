// 定义模块:
var app = angular.module("pinyougou",[]);

app.filter('cutStr', function() {
    return function(text) {
        return text.split(" ");
    }
});
