'use strict';

angular.module('dniprolabApp')
    .factory('Message', function ($resource, DateUtils) {
        return $resource('api/messages/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.created = DateUtils.convertDateTimeFromServer(data.created);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    })
    .factory('MessageCurrentUser', function ($resource, DateUtils) {
        return $resource('api/user-messages', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.created = DateUtils.convertDateTimeFromServer(data.created);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
