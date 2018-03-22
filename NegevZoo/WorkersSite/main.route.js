app.config(function ($stateProvider, $urlRouterProvider) {
    $stateProvider
        .state('login', {
            url:            '/login',
            templateUrl:    'login/login.html',
            controller:     'loginCtrl'
        })
        .state('mainMenu', {
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