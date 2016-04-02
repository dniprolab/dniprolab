'use strict';

angular.module('dniprolabApp')
    .factory('AdvertSearch', function ($resource) {
        return $resource('api/_search/adverts/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
