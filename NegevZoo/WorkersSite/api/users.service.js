app.factory('usersService', ['httpService', function (httpService) {
    var serviceBaseUrl = 'users';

    var userService = {
        login:          function (username, password)   { return httpService.httpGet({ url: [serviceBaseUrl, 'login', username, password] }); },
        getAllUsers:    function ()                     { return httpService.httpGet({ url: [serviceBaseUrl, 'all'] }); },
        updateUser:     function (user)                 { return httpService.httpPost({ url: [serviceBaseUrl, 'add'], body: user }); },
        deleteUser:     function (userId)               { return httpService.httpDelete({ url: [serviceBaseUrl, 'delete', userId] }); },
        updateUsername: function (userId, username)     { return httpService.httpPost({ url: [serviceBaseUrl, 'update', 'name', userId, username] }) },
        updatePassword: function (userId, password)     { return httpService.httpPost({ url: [serviceBaseUrl, 'update', 'password', userId, password] }) }
    }
    
    return userService;
}]);