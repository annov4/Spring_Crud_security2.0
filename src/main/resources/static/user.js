$(document).ready(function () {
    loadUser();

    function loadUser() {
        $.ajax({
            url: '/api/user',
            method: 'GET',
            success: function (user) {
                let tableBodyUser = $('#tableBodyUser');
                tableBodyUser.empty();
                tableBodyUser.append(`
                        <tr>
                            <td>${user.id}</td>
                            <td>${user.age}</td>
                            <td>${user.email}</td>
                            <td>${user.name}</td>
                            <td>${user.role.map(r => r.role.substring(5)).join(', ')}</td>
                        </tr>
                    `);
            },
        });
    }
});