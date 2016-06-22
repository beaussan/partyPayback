(function() {
    'use strict';
    angular
        .module('partyPaybackApp')
        .factory('ExtandedUser', ExtandedUser);

    ExtandedUser.$inject = ['$resource'];

    function ExtandedUser ($resource) {
        var resourceUrl =  'api/extanded-users/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
