layui.define(["form"], function (exports) {
    var form = layui.form,
        Address = function () {
        };
    var addressData;
    Address.prototype.country = function () {
        var that = this;
        form.on('select(country2)', function (countryData) {
            var countryCode = countryData.value;
            if (countryCode) {
                that.getAddressData(countryCode);
                that.emptySelect("city2");
                that.emptySelect("area2");
            } else {
                $("#province2").attr("disabled", "disabled");
            }
        });

        var country1 = $("#country2").val();
        if (country1) {
            that.getAddressData(country1);
        }
    }

    Address.prototype.emptySelect = function (id) {
        var proHtml = '<option value="">Pls select ...</option>';
        $("#" + id).empty();
        $("#" + id).append(proHtml);

    }

    Address.prototype.getAddressData = function (countryCode) {
        var that = this;
        $.get("../../../address/" + countryCode + ".json", function (data) {
            addressData = data;

            var province1 = $("#province2").val();
            var city1 = $("#city2").val();
            var area1 = $("#area2").val();
            that.provinces(province1);
            var citys, areas;
            if (province1) {
                for (var i = 0; i < addressData.length; i++) {
                    if (province1 == addressData[i].code) {
                        citys = addressData[i].childs;
                        that.citys(citys, city1);


                    }
                }
            }
            if (city1) {
                for (var j = 0; j < citys.length; j++) {
                    if (city1 == citys[j].code) {
                        areas = citys[j].childs;
                        that.areas(areas, area1);
                    }
                }
            }

        })
    }

    Address.prototype.provinces = function (province) {
        //加载省数据
        var that = this;
        var proHtml = '<option value="">Pls select ...</option>';
        for (var i = 0; i < addressData.length; i++) {
            if (province == addressData[i].code) {
                proHtml += '<option value="' + addressData[i].code + '" selected>' + addressData[i].name + '</option>';
            } else {
                proHtml += '<option value="' + addressData[i].code + '">' + addressData[i].name + '</option>';
            }
        }
        $("#province2").empty();
        //初始化省数据
        $("#province2").append(proHtml);
        form.render();
        form.on('select(province2)', function (proData) {
            $("#area2").html('<option value="">Pls select ...</option>');
            var value = proData.value;
            if (value > 0) {
                that.citys(addressData[$(this).index() - 1].childs);
            } else {
                $("#city2").attr("disabled", "disabled");
            }
        });
    }

    //加载市数据
    Address.prototype.citys = function (citys, cityValue) {
        var cityHtml = '<option value="">Pls select ...</option>', that = this;
        for (var i = 0; i < citys.length; i++) {
            if (cityValue == citys[i].code) {
                cityHtml += '<option value="' + citys[i].code + '" selected>' + citys[i].name + '</option>';
            } else {
                cityHtml += '<option value="' + citys[i].code + '">' + citys[i].name + '</option>';
            }
        }
        $("#city2").html(cityHtml).removeAttr("disabled");
        form.render();
        form.on('select(city2)', function (cityData) {
            var value = cityData.value;
            if (value > 0) {
                that.areas(citys[$(this).index() - 1].childs);
            } else {
                $("#area2").attr("disabled", "disabled");
            }
        });
    }

    //加载县/区数据
    Address.prototype.areas = function (areas,areaValue) {
        var areaHtml = '<option value="">Pls select ..</option>';
        for (var i = 0; i < areas.length; i++) {
            if (areaValue == areas[i].code) {
                areaHtml += '<option value="' + areas[i].code + '" selected>' + areas[i].name + '</option>';
            } else {
                areaHtml += '<option value="' + areas[i].code + '">' + areas[i].name + '</option>';
            }
        }
        $("#area2").html(areaHtml).removeAttr("disabled");
        form.render();
    }

    var address2 = new Address();
    exports("address2", function () {
        address2.country();
    });
})
