'use strict';

angular.module('dniprolabApp')
    .factory('AdvertisementSearch', function ($resource) {
        return $resource('api/_search/advertisements/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
