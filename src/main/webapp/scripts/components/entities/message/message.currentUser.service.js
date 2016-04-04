/**
 * Created by Max on 04.04.2016.
 */
'use strict';

angular.module('dniprolabApp')
    .factory('MessageCurrentUser', function ($resource, DateUtils) {
        return $resource('api/messages/currentuser/:login', {}, {
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
