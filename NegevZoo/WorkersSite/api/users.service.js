app.factory('usersService', ['httpService', function (httpService) {
    var serviceBaseUrl = 'users';

    var userService = {
        login:          function (language) { return httpService.httpGet({ url: [serviceBaseUrl, 'all', language] }); },
        getAllUsers:    function ()         { return httpService.httpGet({ url: [serviceBaseUrl, 'all'] }); },
        updateUser: function (user)         { return httpService.httpPost({ url: [serviceBaseUrl, 'update'], body: user }); },
        deleteUser: function (userId)       { return httpService.httpDelete({ url: [serviceBaseUrl, 'delete', userId] }); }
    }
    
    return userService;
}]);