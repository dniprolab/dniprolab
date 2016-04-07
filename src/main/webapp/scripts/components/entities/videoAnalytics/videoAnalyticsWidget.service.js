'use strict';

angular.module('dniprolabApp')
    .factory('VideoAnalyticsWidget', function ($resource, DateUtils) {
        return $resource('api/user-videos', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertLocaleDateFromServer(data.date);
                    return data;
                }
            }
        });
    });
