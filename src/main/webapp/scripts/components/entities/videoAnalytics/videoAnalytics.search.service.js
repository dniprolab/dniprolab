'use strict';

angular.module('dniprolabApp')
    .factory('VideoAnalyticsSearch', function ($resource) {
        return $resource('api/_search/videoAnalyticss/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
