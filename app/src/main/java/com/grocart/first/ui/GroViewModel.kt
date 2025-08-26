package com.grocart.first.ui


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.grocart.first.data.InternetItem
import com.grocart.first.network.FirstApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


// ViewModel class to hold and manage UI-related data for the StartScreen
class GroViewModel: ViewModel() {

    // Private mutable state flow to store the internal UI state
    private val _uiState = MutableStateFlow(GroUiState())

    // Public read-only version of the state flow exposed to the UI
    val uiState: StateFlow<GroUiState> = _uiState.asStateFlow()


    private val _isVisible = MutableStateFlow(true)
    val isVisible = _isVisible
    var itemUiState : ItemUiState by mutableStateOf(ItemUiState.Loading)
        private set

    private val _user = MutableStateFlow<FirebaseUser?>(null)
    val user: StateFlow<FirebaseUser?> get() = _user

    private val _phoneNumber = MutableStateFlow("")
    val phoneNumber: StateFlow<String> get() = _phoneNumber

    private val _cartItems = MutableStateFlow<List<InternetItem>>(emptyList())
    val cartItems: StateFlow<List<InternetItem>> get() = _cartItems.asStateFlow()
    private val  _otp = MutableStateFlow("")
    val otp: MutableStateFlow<String> get() = _otp

    private val _verificationId = MutableStateFlow("")
    val verificationId: MutableStateFlow<String> get() = _verificationId
    private  val _ticks = MutableStateFlow(60L)
    val ticks: MutableStateFlow<Long> get() = _ticks

    private val _loading = MutableStateFlow(false)
    val loading : MutableStateFlow<Boolean> get() = _loading

    private val _logoutClicked = MutableStateFlow(false)
    val logoutClicked: MutableStateFlow<Boolean> get() = _logoutClicked

    private lateinit var  timerJob: Job

    // Write a message to the database
    private val database = Firebase.database
    private val myRef = database.getReference("users/${auth.currentUser?.uid}/cart")
    private lateinit var internetJob: Job
    private var screenJob: Job

    sealed interface ItemUiState {
        data class Success(val items: List<InternetItem>) : ItemUiState
        data object Error : ItemUiState
        data object Loading : ItemUiState
    }

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun setOtp(otp: String){
        _otp.value = otp
    }
    fun setVerificationId(verificationId: String){
        _verificationId.value = verificationId
    }

    fun setUser(user: FirebaseUser?)
    {
        _user.value = user
    }
    fun clearData(){
        _user.value = null
        _phoneNumber.value = ""
        _otp.value = ""
        _verificationId.value = ""
        resetTime()
    }
    fun runTimer(){
        timerJob = viewModelScope.launch {
            while (_ticks.value > 0){
                delay(1000)
                _ticks.value-=1
            }
        }
    }

    fun setLoading(isLoading : Boolean){
        _loading.value = isLoading

    }
    fun setLogoutClicked(clicked: Boolean){
        _logoutClicked.value = clicked
    }

    fun resetTime(){
        try {
            timerJob.cancel()
        }catch (_: Exception){

        }finally {
            _ticks.value = 60L
        }
    }

    // Function to update the clickStatus when a category card is clicked
    fun addToCart(item: InternetItem){
        _cartItems.value = _cartItems.value + item

    }

    fun addToDatabase(item: InternetItem){
        myRef.push().setValue(item)

    }
    private fun fillCartItems(){
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                _cartItems.value = emptyList()

                for (childSnapshot in dataSnapshot.children) {
                    val item = childSnapshot.getValue(InternetItem::class.java)
                    item?.let{
                        val newItem = it
                        addToCart(newItem)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
    fun removeFromCart(oldItem: InternetItem){
        myRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                for (childSnapshot in dataSnapshot.children) {
                    var itemRemoved = false
                    val item = childSnapshot.getValue(InternetItem::class.java)
                    item?.let{
                        if(oldItem.itemName == it.itemName && oldItem.itemCategory == it.itemCategory){
                            childSnapshot.ref.removeValue()
                            itemRemoved = true
                        }
                    }
                    if(itemRemoved) break
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
    fun updateClickText(updatedText: String) {
        _uiState.update {
            // Copy the current state and replace only the clickStatus
            it.copy(
                clickStatus = updatedText
            )
        }
    }


    fun updateSelectedCategory(updatedCategory: Int){
        _uiState.update{
            it.copy(
                selectedCategory = updatedCategory
            )
        }
    }
    private fun toggleVisibility(){
        _isVisible.value = false
    }
    fun getFirstItem(){
        internetJob = viewModelScope.launch {
            try {
                val listResult = FirstApi.retrofitService.getItems()
                itemUiState = ItemUiState.Success(listResult)

            }
            catch (_: Exception){
                itemUiState = ItemUiState.Error
                toggleVisibility()
                screenJob.cancel()

            }

        }
    }
    init{
        screenJob = viewModelScope.launch(Dispatchers.Default) {
            delay(3000)
            toggleVisibility()
        }
        getFirstItem()
        fillCartItems()
    }
}

