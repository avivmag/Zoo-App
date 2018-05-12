var app = angular.module('visitors_app', ['ngMaterial', 'ui.router', 'youtube-embed']);

app.config(function ($mdDateLocaleProvider, $httpProvider) {
    $mdDateLocaleProvider.formatDate = function (date) {
        return moment(date).format('DD-MM-YYYY');
    };

    $httpProvider.defaults.withCredentials = true;
});

app.baseURL = 'http://negevzoo.sytes.net:50000/';

app.run(function($rootScope, $cookies, $state) {
    $rootScope.$on('$locationChangeStart', function (event, next, prev) {
            if (!next.endsWith('login')) {
                var sessionValue = $cookies.get('session-id'); 
                if (sessionValue === null || sessionValue === undefined) {
                    $state.go('login');
                }
            }
    });
});