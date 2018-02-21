var app = angular.module('visitors_app', ['ngMaterial', 'ui.router']);

app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('login', {
            url:            '/login',
            templateUrl:    'login/login.html',
            controller:     'loginCtrl'
        })
        .state('home', {
            url:            '/mainMenu',
            templateUrl:    'mainMenu/mainMenu.html',
            controller:     'mainMenuCtrl'
        })
        .state('about', {
            url:            '/about',
            templateUrl:    'about.html'
        })
        .state('contact', {
            url:            '/contact',
            templateUrl:    'contact.html'
        });
});