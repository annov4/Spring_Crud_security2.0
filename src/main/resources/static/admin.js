$(document).ready(function () {
    loadUsers();

    function loadUsers() {
        $.ajax({
            url: '/users',
            method: 'GET',
            success: function (users) {
                var userTableBody = $('#userTableBody');
                userTableBody.empty();
                $.each(users, function (index, user) {
                    userTableBody.append(
                        `<tr>
                                <td>${user.id}</td>
                                <td>${user.username}</td>
                                <td>${user.age}</td>
                                <td>${user.email}</td>
                                <td>${user.role}</td>
                                <td><button class="editUserBtn" data-id="${user.id}">Edit</button></td>
                                <td><button class="deleteUserBtn" data-id="${user.id}">Delete</button></td>
                                </tr>`
                    );
                });
            }
        });
    }

    $('#newUserLink').click(function () {
        $('#userForm')[0].reset();
        $('#userId').val('');
        $('#modalTitle').text('Add User');
        $('#userModal').modal('show');
    });

    $(document).on('click', '.editUserBtn', function () {
        var userId = $(this).data('id');
        $.ajax({
            url: `/user/${userId}`,
            method: 'GET',
            success: function (user) {
                $('#updateDeleteUserId').val(user.id);
                $('#updateDeleteUsername').val(user.username);
                $('#updateDeleteAge').val(user.age);
                $('#updateDeleteEmail').val(user.email);
                $('#updateDeletePassword').val(user.password);
                $('#updateDeleteRole').val(user.role);
                $('#modalTitle').text('Edit User');
                $('#updateDeleteModal').modal('show');
            }
        });
    });

    $('#updateUserBtn').click(function () {
        var userId = $('#updateDeleteUserId').val();
        var user = {
            id: userId,
            username: $('#updateDeleteUsername').val(),
            age: $('#updateDeleteAge').val(),
            email: $('#updateDeleteEmail').val(),
            password: $('#updateDeletePassword').val(),
            role: $('#updateDeleteRole').val()
        };
        $.ajax({
            url: `/user-update`,
            method: 'PUT',
            contentType: 'application/json',
            data: JSON.stringify(user),
            success: function () {
                $('#updateDeleteModal').modal('hide');
                loadUsers();
            }
        });
    });

    $(document).on('click', '.deleteUserBtn', function () {
        if (confirm('Delete user?')) {
            var userId = $(this).data('id');
            $.ajax({
                url: `/user-delete/${userId}`,
                method: 'DELETE',
                success: function () {
                    loadUsers();
                }
            });
        }
    });

    $('#userForm').submit(function (event) {
        event.preventDefault();
        var userId = $('#userId').val();
        var user = {
            username: $('#username').val(),
            age: $('#age').val(),
            email: $('#email').val(),
            password: $('#password').val(),
            role: $('#role').val()
        };
        if (userId) {
            $.ajax({
                url: `/user-update`,
                method: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(user),
                success: function () {
                    $('#userModal').modal('hide');
                    loadUsers();
                }
            });
        } else {
            $.ajax({
                url: '/user-create',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(user),
                success: function () {
                    $('#userModal').modal('hide');
                    loadUsers();
                }
            });
        }
    });
});