$(document).ready(function() {


    $('#initializeData').click(function() {
        initData();
    });

    function initData(){
        $('.right-column').removeClass('blur');

        $.ajax({
            url: '/categories',
            type: 'GET',
            success: function(categories) {
                if(categories.length === 0){
                    initCategory();
                    initBrand();
                    initProducts();

                    alert("데이터 초기셋팅 완료");
                }
                initLoadedData();
                logResponse(this.url, this.type, categories);
            },
            error: function(xhr, status, error) {
                logFailResponse(xhr, status, error);
            }
        });
    }


    function initProducts(){
        const products = [
            { brandName: 'A', categoryId: 1, price: 11200 },
            { brandName: 'A', categoryId: 2, price: 5500 },
            { brandName: 'A', categoryId: 3, price: 4200 },
            { brandName: 'A', categoryId: 4, price: 9000 },
            { brandName: 'A', categoryId: 5, price: 2000 },
            { brandName: 'A', categoryId: 6, price: 1700 },
            { brandName: 'A', categoryId: 7, price: 1800 },
            { brandName: 'A', categoryId: 8, price: 2300 },
            { brandName: 'B', categoryId: 1, price: 10500 },
            { brandName: 'B', categoryId: 2, price: 5900 },
            { brandName: 'B', categoryId: 3, price: 3800 },
            { brandName: 'B', categoryId: 4, price: 9100 },
            { brandName: 'B', categoryId: 5, price: 2100 },
            { brandName: 'B', categoryId: 6, price: 2000 },
            { brandName: 'B', categoryId: 7, price: 2000 },
            { brandName: 'B', categoryId: 8, price: 2200 },
            { brandName: 'C', categoryId: 1, price: 10000 },
            { brandName: 'C', categoryId: 2, price: 6200 },
            { brandName: 'C', categoryId: 3, price: 3300 },
            { brandName: 'C', categoryId: 4, price: 9200 },
            { brandName: 'C', categoryId: 5, price: 2200 },
            { brandName: 'C', categoryId: 6, price: 1900 },
            { brandName: 'C', categoryId: 7, price: 2200 },
            { brandName: 'C', categoryId: 8, price: 2100 },
            { brandName: 'D', categoryId: 1, price: 10100 },
            { brandName: 'D', categoryId: 2, price: 5100 },
            { brandName: 'D', categoryId: 3, price: 3000 },
            { brandName: 'D', categoryId: 4, price: 9500 },
            { brandName: 'D', categoryId: 5, price: 2500 },
            { brandName: 'D', categoryId: 6, price: 1500 },
            { brandName: 'D', categoryId: 7, price: 2400 },
            { brandName: 'D', categoryId: 8, price: 2000 },
            { brandName: 'E', categoryId: 1, price: 10700 },
            { brandName: 'E', categoryId: 2, price: 5000 },
            { brandName: 'E', categoryId: 3, price: 3800 },
            { brandName: 'E', categoryId: 4, price: 9900 },
            { brandName: 'E', categoryId: 5, price: 2300 },
            { brandName: 'E', categoryId: 6, price: 1800 },
            { brandName: 'E', categoryId: 7, price: 2100 },
            { brandName: 'E', categoryId: 8, price: 2100 },
            { brandName: 'F', categoryId: 1, price: 11200 },
            { brandName: 'F', categoryId: 2, price: 7200 },
            { brandName: 'F', categoryId: 3, price: 4000 },
            { brandName: 'F', categoryId: 4, price: 9300 },
            { brandName: 'F', categoryId: 5, price: 2100 },
            { brandName: 'F', categoryId: 6, price: 1600 },
            { brandName: 'F', categoryId: 7, price: 2300 },
            { brandName: 'F', categoryId: 8, price: 1900 },
            { brandName: 'G', categoryId: 1, price: 10500 },
            { brandName: 'G', categoryId: 2, price: 5800 },
            { brandName: 'G', categoryId: 3, price: 3900 },
            { brandName: 'G', categoryId: 4, price: 9000 },
            { brandName: 'G', categoryId: 5, price: 2200 },
            { brandName: 'G', categoryId: 6, price: 1700 },
            { brandName: 'G', categoryId: 7, price: 2100 },
            { brandName: 'G', categoryId: 8, price: 2000 },
            { brandName: 'H', categoryId: 1, price: 10800 },
            { brandName: 'H', categoryId: 2, price: 6300 },
            { brandName: 'H', categoryId: 3, price: 3100 },
            { brandName: 'H', categoryId: 4, price: 9700 },
            { brandName: 'H', categoryId: 5, price: 2100 },
            { brandName: 'H', categoryId: 6, price: 1600 },
            { brandName: 'H', categoryId: 7, price: 2000 },
            { brandName: 'H', categoryId: 8, price: 2000 },
            { brandName: 'I', categoryId: 1, price: 11400 },
            { brandName: 'I', categoryId: 2, price: 6700 },
            { brandName: 'I', categoryId: 3, price: 3200 },
            { brandName: 'I', categoryId: 4, price: 9500 },
            { brandName: 'I', categoryId: 5, price: 2400 },
            { brandName: 'I', categoryId: 6, price: 1700 },
            { brandName: 'I', categoryId: 7, price: 1700 },
            { brandName: 'I', categoryId: 8, price: 2400 },
        ];

        products.forEach(function(product) {
            $.ajax({
                url: '/products',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    brandName: product.brandName,
                    categoryId: product.categoryId,
                    price: product.price
                }),
                success: function(response) {
                    logResponse(this.url, this.type, response);
                },
                error: function(xhr, status, error) {
                    logFailResponse(xhr, status, error);
                }
            });
        });
    }

    function initCategory(){
        const categories = [
            { id: 1, name: "상의" },
            { id: 2, name: "아우터" },
            { id: 3, name: "바지" },
            { id: 4, name: "스니커즈" },
            { id: 5, name: "가방" },
            { id: 6, name: "모자" },
            { id: 7, name: "양말" },
            { id: 8, name: "액세서리" }
        ];

        categories.forEach(function(category) {
            $.ajax({
                url: '/categories',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({
                    id: category.id,
                    name: category.name
                }),
                success: function(response) {
                    logResponse(this.url, this.type, response);
                },
                error: function(xhr, status, error) {
                    logFailResponse(xhr, status, error);
                }
            });
        });
    }

    function initBrand(){
        const brands = [
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "I"
        ];

        brands.forEach(function(brand) {
            $.ajax({
                url: '/brands',
                type: 'POST',
                contentType: 'application/json',
                data: brand,
                success: function(response) {
                    logResponse(this.url, this.type, response);
                },
                error: function(xhr, status, error) {
                    logFailResponse(xhr, status, error);
                }
            });
        });
    }

    function initLoadedData(){
        loadAllProducts();

        loadCategories('상의');
        loadLowestPriceProducts();
        loadLowestPriceBrandForAllCategories();
        loadPriceRange('상의');
    }

    function loadAllProducts(){
        $.ajax({
            url: '/products',
            type: 'GET',
            success: function(products) {
                $('#initializeData').parent().hide();
                $('#productTableContainer').show();
                $('#lowestPriceTableContainer').show();
                $('#refreshLowestPrice').show();
                $('#lowestPriceBrandTable').show();


                const productTableBody = $('#productTable tbody');
                productTableBody.empty();
                const brands = {};

                products.forEach(function(product) {
                    const brandName = product.brand.name;
                    const categoryName = product.category.name;
                    const price = product.price;

                    if (!brands[brandName]) {
                        brands[brandName] = {
                            상의: '',
                            아우터: '',
                            바지: '',
                            스니커즈: '',
                            가방: '',
                            모자: '',
                            양말: '',
                            액세서리: ''
                        };
                    }

                    brands[brandName][categoryName] = price;
                });

                for (const brand in brands) {
                    const row = $('<tr>').attr('class',brand+'-row');
                    row.append($('<td>').text(brand));
                    row.append($('<td>').attr('id', brand + '-상의').text(brands[brand].상의 || ''));
                    row.append($('<td>').attr('id', brand + '-아우터').text(brands[brand].아우터 || ''));
                    row.append($('<td>').attr('id', brand + '-바지').text(brands[brand].바지 || ''));
                    row.append($('<td>').attr('id', brand + '-스니커즈').text(brands[brand].스니커즈 || ''));
                    row.append($('<td>').attr('id', brand + '-가방').text(brands[brand].가방 || ''));
                    row.append($('<td>').attr('id', brand + '-모자').text(brands[brand].모자 || ''));
                    row.append($('<td>').attr('id', brand + '-양말').text(brands[brand].양말 || ''));
                    row.append($('<td>').attr('id', brand + '-액세서리').text(brands[brand].액세서리 || ''));
                    productTableBody.append(row);
                }
                logResponse(this.url, this.type, products);
            },
            error: function(xhr, status, error) {
                logFailResponse(xhr, status, error);
            }
        });
    }

    $('#refreshLowestPrice').click(function() {
        loadAllProducts();
        loadLowestPriceProducts();
        loadLowestPriceBrandForAllCategories();
        loadPriceRange('상의');
    });

    $('#test1').click(function() {
        upsertProducts('I', 1, 1000);
        setTimeout(function () {
            loadAllProducts();
            loadCategories('상의');
            loadLowestPriceProducts();
            loadLowestPriceBrandForAllCategories();
            loadPriceRange('상의');

            setTimeout(function () {
                $('.highlight').removeClass('highlight');
                $('#I-상의').addClass('highlight');
                $('.I-상의').addClass('highlight');
                $('.I-brand').addClass('highlight');
                $('.I').addClass('highlight');
            }, 200);
        }, 200);
    });

    $('#test2').click(function() {
        upsertProducts('A', 4, 10000);
        setTimeout(function () {
            loadAllProducts();
            loadCategories('스니커즈');
            loadLowestPriceProducts();
            loadLowestPriceBrandForAllCategories();
            loadPriceRange('스니커즈');
            setTimeout(function () {
                $('.highlight').removeClass('highlight');
                $('#A-스니커즈').addClass('highlight');
                $('.A').addClass('highlight')
            }, 200);
        }, 200);


    });

    $('#test3').click(function() {
        $.ajax({
            url: '/brands',
            type: 'POST',
            contentType: 'application/json',
            data: 'J',
            success: function(response) {
                const products = [
                    { brandName: 'J', categoryId: 1, price: 9000 },
                    { brandName: 'J', categoryId: 2, price: 7500 },
                    { brandName: 'J', categoryId: 3, price: 2000 },
                    { brandName: 'J', categoryId: 4, price: 11000 },
                    { brandName: 'J', categoryId: 5, price: 1900 },
                    { brandName: 'J', categoryId: 6, price: 2500 },
                    { brandName: 'J', categoryId: 7, price: 1500 },
                    { brandName: 'J', categoryId: 8, price: 2800 }
                ];

                products.forEach(function(product) {
                    $.ajax({
                        url: '/products',
                        type: 'POST',
                        contentType: 'application/json',
                        data: JSON.stringify({
                            brandName: product.brandName,
                            categoryId: product.categoryId,
                            price: product.price
                        }),
                        success: function(response) {
                            $('#test3').prop('disabled', true);
                        },
                        error: function(xhr, status, error) {
                            logFailResponse(xhr, status, error);
                        }
                    });
                });
                logResponse(this.url, this.type, response);
            },
            error: function(xhr, status, error) {
                logFailResponse(xhr, status, error);
            }
        });
        setTimeout(function () {
            loadAllProducts();
            loadCategories('상의');
            loadLowestPriceProducts();
            loadLowestPriceBrandForAllCategories();
            loadPriceRange('상의');
            setTimeout(function () {
                $('.highlight').removeClass('highlight');
                $('.J').addClass('highlight')
                $('.J-row').addClass('highlight')
            }, 200);
        }, 200);

    });

    function upsertProducts(brandName, categoryId, price){
        console.log(brandName, categoryId, price);
        $.ajax({
            url: '/products',
            type: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify({
                brandName: brandName,
                categoryId: categoryId,
                price: price
            }),
            success: function(response) {
                logResponse(this.url, this.type, response);
            },
            error: function(xhr, status, error) {
                logFailResponse(xhr, status, error);
            }
        });
    }

    function loadLowestPriceProducts() {
        $.ajax({
            url: '/products/lowest-price',
            type: 'GET',
            success: function(response) {
                const lowestPriceTableBody = $('#lowestPriceTable tbody');
                lowestPriceTableBody.empty();

                let totalPrice = 0;

                Object.keys(response).forEach(function(key) {
                    if (key === "총액") {
                        totalPrice = response[key].totalPrice;
                    } else {
                        const item = response[key];
                        const row = $('<tr>').attr('class', item.brand+"-"+key);
                        row.append($('<td>').text(key));
                        row.append($('<td>').text(item.brand));
                        row.append($('<td>').text(item.price));
                        lowestPriceTableBody.append(row);
                    }
                });

                $('#totalPrice').text(totalPrice.toLocaleString());
                logResponse(this.url, this.type, response);
            },
            error: function(xhr, status, error) {
                logFailResponse(xhr, status, error);
            }
        });
    }

    function loadLowestPriceBrandForAllCategories() {
        $.ajax({
            url: '/products/lowest-price-brand',
            type: 'GET',
            success: function(response) {
                const brandData = response['최저가'];
                $('#lowestBrand').attr('class', brandData.브랜드+"-brand");
                $('#lowestBrand').text(brandData.브랜드);

                const categories = brandData.카테고리;
                const lowestPriceBrandTableBody = $('#lowestPriceBrandTableBody');
                lowestPriceBrandTableBody.empty();

                let totalPrice = 0;

                categories.forEach(function(item) {
                    const row = $('<tr>');
                    row.append($('<td>').text(item.카테고리));
                    row.append($('<td>').text(item.가격));
                    lowestPriceBrandTableBody.append(row);

                    totalPrice += parseInt(item.가격);
                });

                $('#lowestTotalPrice').text(totalPrice.toLocaleString());
                $('#lowestPriceBrandTable').show();

                logResponse(this.url, this.type, response);
            },
            error: function(xhr, status, error) {
                logFailResponse(xhr, status, error);
            }
        });
    }

    function loadCategories(value) {
        $.ajax({
            url: '/categories',
            type: 'GET',
            success: function(categories) {
                makeCategorySelectBox(categories)
                $('#categorySelect').val(value)
                logResponse(this.url, this.type, categories);
            },
            error: function(xhr, status, error) {
                logFailResponse(xhr, status, error);
            }
        });
    }

    function makeCategorySelectBox(categories){
        categories.sort((a, b) => a.id - b.id);
        const categorySelect = $('#categorySelect');
        $('#categorySelect').empty();

        categories.forEach(function(category) {
            categorySelect.append($('<option>').val(category.name).text(category.name));
        });
    }

    $('#categorySelect').change(function() {
        const selectedCategory = $(this).val();
        if (selectedCategory) {
            loadPriceRange(selectedCategory);
        }
    });

    function loadPriceRange(category) {
        $.ajax({
            url: '/products/price-range',
            type: 'GET',
            data: { categoryName: category },
            success: function(response) {
                const lowestPrice = response.최저가[0];
                const highestPrice = response.최고가[0];

                $('#lowestPriceBrand').attr('class', lowestPrice.브랜드);
                $('#highestPriceBrand').attr('class', highestPrice.브랜드);

                $('#lowestPriceBrand').text(`[브랜드 ${lowestPrice.브랜드}]  ${lowestPrice.가격}원`);
                $('#highestPriceBrand').text(`[브랜드 ${highestPrice.브랜드}]  ${highestPrice.가격}원`);

                logResponse(this.url, this.type, response);
            },
            error: function(xhr, status, error) {
                logFailResponse(xhr, status, error);
            }
        });
    }

    function logResponse(url, type, response) {
        const currentTime = new Date().toLocaleString();

        const responseString = JSON.stringify(response, null, 2);
        const truncatedResponse = responseString.length > 50 ? responseString.substring(0, 50) + '...이하 생략' : responseString;

        const logEntry = `[${currentTime}] [${type} : ${url}] \n${truncatedResponse}\n`;

        $('#responseLog').val(function(i, text) {
            return text + logEntry;
        });

        const responseLog = $('#responseLog');
        responseLog.scrollTop(responseLog[0].scrollHeight);
    }



    function logFailResponse(xhr, status, error) {
        const logEntry = `[Error] URL: ${xhr.responseURL}\nStatus: ${status}\nError: ${error}\nResponse: ${xhr.responseText}\n\n`;
        $('#responseLog').val(function(i, text) {
            return text + logEntry;
        });
    }

});