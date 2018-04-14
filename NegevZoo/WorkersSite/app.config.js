var app = angular.module('visitors_app', ['ngMaterial', 'ui.router', 'youtube-embed']);

app.config(function ($mdDateLocaleProvider) {
    $mdDateLocaleProvider.formatDate = function (date) {
        return moment(date).format('DD-MM-YYYY');
    };
});

app.baseURL = 'http://negevzoo.sytes.net:50000/';