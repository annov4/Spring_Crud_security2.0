const url = 'http://localhost:8080/api/admin';

function getUserData() {
    $.ajax({
        url: url,
        method: 'GET',
        dataType: 'json',
        success: function (data) {
            loadTable(data);
        }
    });
}

function getAllUsers() {
    $.ajax({
        url: url,
        method: 'GET',
        dataType: 'json',
        success: function (user) {
            loadTable(user);
        }
    });
}

function loadTable(listAllUsers) {
    let res = '';
    for (let user of listAllUsers) {
        res += `
            <tr>
                <td>${user.id}</td>
                <td>${user.age}</td>
                <td>${user.email}</td>
                <td>${user.name}</td>
                <td>${user.role.map(r => r.role.substring(5)).join(', ')}</td>
                <td><button class="btn btn-info" onclick="editModal(${user.id})" data-bs-toggle="modal" data-bs-target="#editUserModal">Edit</button></td>
                <td><button class="btn btn-danger" onclick="deleteModal(${user.id})" data-bs-toggle="modal" data-bs-target="#deleteUserModal">Delete</button></td>
            </tr>
        `;
    }
    $('#tableBodyAdmin').html(res);
}

getAllUsers();

// Новый юзер
$('#newUserForm').on('submit', function (e) {
    e.preventDefault();
    let role = $('#role_select');
    let rolesAddUser = [];
    for (let i = 0; i < role[0].options.length; i++) {
        if (role[0].options[i].selected) {
            rolesAddUser.push({id: role[0].options[i].value, name: 'ROLE_' + role[0].options[i].innerHTML});
        }
    }
    $.ajax({
        url: url,
        method: 'POST',
        contentType: 'application/json;charset=utf-8',
        data: JSON.stringify({
            userName: $('#newUserName').val(),
            age: $('#newAge').val(),
            password: $('#newPassword').val(),
            role: rolesAddUser
        }),
        success: function () {
            getUserData();
            $("#all-users-tab").click();
        }
    });
});

// Изменение юзера
function editModal(id) {
    $.ajax({
        url: url + '/' + id,
        method: 'GET',
        contentType: 'application/json;charset=UTF-8',
        success: function (u) {
            $('#editId').val(u.id);
            $('#editUserName').val(u.userName);
            $('#editAge').val(u.age);
            $('#editPassword').val("****");
        }
    });
}

async function editUser() {
    let idValue = $("#editId").val();
    let userNameValue = $("#editUserName").val();
    let ageValue = $('#editAge').val();
    let passwordValue = $("#editPassword").val();

    let roleElements = $('#editRole').get(0).options;
    let roles = [];
    for (let i = 0; i < roleElements.length; i++) {
        if (roleElements[i].selected) {
            roles.push({
                id: roleElements[i].value,
                name: 'ROLE_' + roleElements[i].text
            });
        }
    }

    let user = {
        id: idValue,
        userName: userNameValue,
        age: ageValue,
        password: passwordValue,
        role: roles,
    };

    await $.ajax({
        url: url + '/' + user.id,
        method: 'PUT',
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(user),
        success: function () {
            closeModal();
            getUserData();
        }
    });
}

// Удаление юзера
function deleteModal(id) {
    $.ajax({
        url: url + '/' + id,
        method: 'GET',
        contentType: 'application/json;charset=UTF-8',
        success: function (u) {
            $('#deleteId').val(u.id);
            $('#deleteUserName').val(u.userName);
            $('#deleteAge').val(u.age);
            $('#deleteRole').val(u.role.map(r => r.role.substring(5)).join(", "));
        }
    });
}

async function deleteUser() {
    const id = $("#deleteId").val();
    let urlDel = url + "/" + id;

    await $.ajax({
        url: urlDel,
        method: 'DELETE',
        contentType: 'application/json',
        success: function () {
            closeModal();
            getUserData();
        }
    });
}

function closeModal() {
    $(".btn-close").click();
}