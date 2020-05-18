package com.example.adiapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SearchRecentSuggestionsProvider;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.adiapp.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity  {


  private Button createAcc;
  private Button singIn;
  private FirebaseAuth auth;
  private FirebaseDatabase db;
  DatabaseReference user;
  private RelativeLayout root;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        user=db.getReference("Users");
        //Кнопка регистрации
        createAcc = (Button) findViewById(R.id.create_acc_btn);
        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterWindow();
            }
        });
        //Кнопка входа
        singIn = (Button) findViewById(R.id.sing_in_btn);
        singIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSignInWindow();
            }
        });

        root = findViewById(R.id.root_element);


    }



    //Окно входа
    public void showSignInWindow() {

        //Диалоговое окно
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Войти");
        builder.setMessage("Введите данные для входа");
        //Cоздание объекта  View из Layout
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View sing_in = layoutInflater.inflate(R.layout.sing_in, null);
        final CheckBox checkBox=(CheckBox) sing_in.findViewById(R.id.checkbox1);
        builder.setView(sing_in);

        //Связываем EditTexts
        final MaterialEditText email = sing_in.findViewById(R.id.email_editText);
        final MaterialEditText password = sing_in.findViewById(R.id.password_editText);

        //Используем кнопу "Отменить",чтобы скрыть диалоговое окно
        builder.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //Используем кнопку "Зарегистрироваться" для аутентификации пользователя
        builder.setPositiveButton("Войти", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введите пароль,которые более 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                    //Авторизуем пользователя
                    auth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                //Если пользовательно ввёл данные успещно,то выполняется переход на  активити RootMenu
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                        startActivity(new Intent(MainActivity.this,RootMenu.class));
                                        finish();
                                }
                                //Если данные введены неверно,выскакивает ошибка
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(root,"Ошибка авторизации. " + e.getMessage(),Snackbar.LENGTH_LONG).show();
                            return;
                        }
                    });

            }
        });

        builder.show();

    }


    //Окно регистрации
    public void showRegisterWindow() {
        //Диалоговое окно
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Зарегистрироваться");
        alertDialog.setMessage("Введите данные для регистрации");


        //Cоздание объекта  View из Layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View register_window = inflater.inflate(R.layout.register_window, null);
        alertDialog.setView(register_window);

        //Связываем EditTexts
        final MaterialEditText name = register_window.findViewById(R.id.name_editText);
        final MaterialEditText surname = register_window.findViewById(R.id.surname_editText);
        final MaterialEditText email = register_window.findViewById(R.id.email_editText);
        final MaterialEditText password = register_window.findViewById(R.id.password_editText);

        //Используем кнопу "Отменить",чтобы скрыть диалоговое окно
        alertDialog.setNegativeButton("Отменить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //Используем кнопку "Зарегистрироваться" для регистрации пользователя
        alertDialog.setPositiveButton("Зарегистрироваться", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(root, "Введите вашу почту", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(root, "Введите ваше имя", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(surname.getText().toString())) {
                    Snackbar.make(root, "Введите вашу фамилию", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (password.getText().toString().length() < 5) {
                    Snackbar.make(root, "Введите пароль,которые более 5 символов", Snackbar.LENGTH_SHORT).show();
                    return;
                }


                //Регистрация пользователя!
                auth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Users users = new Users();
                                users.setEmail(email.getText().toString());
                                users.setName(name.getText().toString());
                                users.setSurname(surname.getText().toString());
                                users.setPassword(password.getText().toString());

                                user.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .setValue(users)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Snackbar.make(root, "Пользователь успешно добавлен", Snackbar.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        });

            }
        });

        alertDialog.show();

    }



}


