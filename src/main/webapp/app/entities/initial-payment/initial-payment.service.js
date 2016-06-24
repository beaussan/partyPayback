(function() {
    'use strict';
    angular
        .module('partyPaybackApp')
        .factory('InitialPayment', InitialPayment);

    InitialPayment.$inject = ['$resource'];

    function InitialPayment ($resource) {
        var resourceUrl =  'api/initial-payments/:id';

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
            'byEvent': {
                method: 'GET',
                isArray : true,
                url : "api/initial-payments/event/:eventId",
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
