package app.waynechen.stylish.payment;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import app.waynechen.stylish.R;
import app.waynechen.stylish.Stylish;
import app.waynechen.stylish.data.CartProduct;
import app.waynechen.stylish.util.ImageManager;

import java.util.ArrayList;

import tech.cherri.tpdirect.api.TPDCard;
import tech.cherri.tpdirect.api.TPDForm;
import tech.cherri.tpdirect.api.TPDServerType;
import tech.cherri.tpdirect.api.TPDSetup;
import tech.cherri.tpdirect.callback.TPDTokenFailureCallback;
import tech.cherri.tpdirect.callback.TPDTokenSuccessCallback;
import tech.cherri.tpdirect.model.TPDStatus;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class PaymentAdapter extends RecyclerView.Adapter {

    private static final int TYPE_LOADING   = 0;
    private static final int TYPE_PRODUCT   = 0x01;
    private static final int TYPE_TITLE     = 0x02;
    private static final int TYPE_RECIPIENT = 0x03;
    private static final int TYPE_METHOD    = 0x04;

    private PaymentContract.Presenter mPresenter;
    private ArrayList<CartProduct> mCartProducts = new ArrayList<>();

    public PaymentAdapter(PaymentContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_RECIPIENT) {
            return new RecipientViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_payment_recipient, parent, false));
        } else if (viewType == TYPE_METHOD) {
            return new MethodViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_payment_method, parent, false));
        } else if (viewType == TYPE_PRODUCT) {
            return new ProductViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_payment_product, parent, false));
        } else {
            return new TitleViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_payment_title, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ProductViewHolder) {

            bindProduct((ProductViewHolder) holder, position - 1);

        } else if (holder instanceof RecipientViewHolder) {

            bindRecipient((RecipientViewHolder) holder);

        } else if (holder instanceof MethodViewHolder) {

            bindMethod((MethodViewHolder) holder);
        }
    }

    @Override
    public int getItemCount() {
        return mCartProducts.size() + 3;
    }


    @Override
    public int getItemViewType(int position) {

        if (position == 0) {
            return TYPE_TITLE;
        } else if (position < mCartProducts.size() + 1) {
            return TYPE_PRODUCT;
        } else if (position == mCartProducts.size() + 1) {
            return TYPE_RECIPIENT;
        } else {
            return TYPE_METHOD;
        }
    }

    private void bindProduct(ProductViewHolder holder, int position) {
        ImageManager.getInstance().setImageByUrl(
                holder.getImageMain(), mCartProducts.get(position).getMainImage());

        // Set title
        holder.getTextTitle().setText(mCartProducts.get(position).getTitle());

        holder.getImageColor().setBackground(new ShapeDrawable(new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {

                paint.setColor(android.graphics.Color
                        .parseColor("#" + mCartProducts.get(position).getSelectedColorCode()));
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);

                paint.setColor(android.graphics.Color.BLACK);
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(Stylish.getAppContext().getResources().getDimensionPixelSize(R.dimen.edge_payment_color));
                canvas.drawRect(0, 0, this.getWidth(), this.getHeight(), paint);

            }
        }));

        holder.getTextSize().setText(mCartProducts.get(position).getSelectedSize());

        holder.getTextPrice().setText(Stylish.getAppContext().getString(R.string.nt_dollars_,
                mCartProducts.get(position).getPrice()));

        holder.getTextAmount()
                .setText(Stylish.getAppContext().getString(R.string._payment_amount,
                        mCartProducts.get(position).getSelectedAmount()));
    }

    private void bindRecipient(RecipientViewHolder holder) {

    }

    private void bindMethod(MethodViewHolder holder) {
        // Set Methods Spinner
        holder.getSpinnerMethods().setForeground(new ShapeDrawable(new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {

                paint.setColor(Stylish.getAppContext().getColor(R.color.gray_cccccc));
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(Stylish.getAppContext().getResources().getDimensionPixelSize(R.dimen.underline_payment_spinner));
                canvas.drawLine(0, this.getHeight(), this.getWidth(), this.getHeight(), paint);

            }
        }));

        holder.getSpinnerMethods().setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                holder.getTpdForm().setVisibility((position == 1) ? View.VISIBLE : View.GONE);
                mPresenter.onPaymentSpinnerMethodsChanged(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // Set Total Price
        holder.getTextTotalPrice().setText(Stylish.getAppContext()
                .getString(R.string.nt_dollars_, mPresenter.getPaymentSubTotal()));
        // Set Freight Price
        holder.getTextFreightPrice().setText(Stylish.getAppContext()
                .getString(R.string.nt_dollars_, mPresenter.getPaymentFreight()));
        // Set Total Products
        holder.getTextTotalProducts()
                .setText(Stylish.getAppContext()
                        .getString(R.string.total_products, mCartProducts.size()));
        // Set Price of Total Products
        holder.getTextTotalProductsPrice().setText(Stylish.getAppContext()
                .getString(R.string.nt_dollars_, mPresenter.getPaymentTotal()));


        TPDSetup.initInstance(Stylish.getAppContext(),
                Integer.parseInt(Stylish.getAppContext().getString(R.string.tp_app_id)),
                Stylish.getAppContext().getString(R.string.tp_app_key), TPDServerType.Sandbox);

        holder.getTpdForm().setTextErrorColor(Stylish.getAppContext().getColor(R.color.red_d0021b));
        holder.getTpdForm().setOnFormUpdateListener(tpdStatus -> {

            if (tpdStatus.getCardNumberStatus() == TPDStatus.STATUS_ERROR) {

                mPresenter.onPaymentTpdErrorMessageChanged(
                        Stylish.getAppContext().getString(R.string.tpd_card_number_error));

            } else if (tpdStatus.getExpirationDateStatus() == TPDStatus.STATUS_ERROR) {

                mPresenter.onPaymentTpdErrorMessageChanged(
                        Stylish.getAppContext().getString(R.string.tpd_expiration_date_error));

            } else if (tpdStatus.getCcvStatus() == TPDStatus.STATUS_ERROR) {

                mPresenter.onPaymentTpdErrorMessageChanged(
                        Stylish.getAppContext().getString(R.string.tpd_ccv_error));
            }
            mPresenter.onPaymentCanGetPrimeChanged(tpdStatus.isCanGetPrime());
        });

        TPDTokenSuccessCallback tpdTokenSuccessCallback = (token, tpdCardInfo)
                -> mPresenter.onPaymentPrimeSuccess(token);

        TPDTokenFailureCallback tpdTokenFailureCallback = (status, reportMsg)
                -> mPresenter.onPaymentPrimeFail(String.valueOf(status) + reportMsg);

        holder.tpdCard = TPDCard.setup(holder.getTpdForm())
                            .onSuccessCallback(tpdTokenSuccessCallback)
                                .onFailureCallback(tpdTokenFailureCallback);
    }

    public class TitleViewHolder extends RecyclerView.ViewHolder {

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImageMain;
        private TextView mTextTitle;
        private ImageView mImageColor;
        private TextView mTextSize;
        private TextView mTextPrice;
        private TextView mTextAmount;


        public ProductViewHolder(View itemView) {
            super(itemView);

            mImageMain = itemView.findViewById(R.id.image_payment_main);
            mTextTitle = itemView.findViewById(R.id.text_payment_title);
            mImageColor = itemView.findViewById(R.id.image_payment_color);
            mTextSize = itemView.findViewById(R.id.text_payment_size);
            mTextPrice = itemView.findViewById(R.id.text_payment_price);
            mTextAmount = itemView.findViewById(R.id.text_payment_amount);
        }

        public ImageView getImageMain() {
            return mImageMain;
        }

        public TextView getTextTitle() {
            return mTextTitle;
        }

        public ImageView getImageColor() {
            return mImageColor;
        }

        public TextView getTextSize() {
            return mTextSize;
        }

        public TextView getTextPrice() {
            return mTextPrice;
        }

        public TextView getTextAmount() {
            return mTextAmount;
        }
    }

    public class RecipientViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

        private EditText mEditName;
        private EditText mEditEmail;
        private EditText mEditPhone;
        private EditText mEditAddress;

        private TextView mTextShippingTime;
        private RadioButton mRadioMorning;
        private RadioButton mRadioAfternoon;
        private RadioButton mRadioAnyTime;

        public RecipientViewHolder(@NonNull View itemView) {
            super(itemView);

            mEditName = itemView.findViewById(R.id.edit_payment_recipient_name);
            mEditEmail = itemView.findViewById(R.id.edit_payment_recipient_email);
            mEditPhone = itemView.findViewById(R.id.edit_payment_recipient_phone);
            mEditAddress = itemView.findViewById(R.id.edit_payment_recipient_address);

            mTextShippingTime = itemView.findViewById(R.id.text_payment_shipping_time_title);
            mRadioMorning = itemView.findViewById(R.id.radio_shipping_morning);
            mRadioAfternoon = itemView.findViewById(R.id.radio_shipping_afternoon);
            mRadioAnyTime = itemView.findViewById(R.id.radio_shipping_anytime);

            mEditName.addTextChangedListener(new RecipientTextWatcher(mEditName));
            mEditEmail.addTextChangedListener(new RecipientTextWatcher(mEditEmail));
            mEditPhone.addTextChangedListener(new RecipientTextWatcher(mEditPhone));
            mEditAddress.addTextChangedListener(new RecipientTextWatcher(mEditAddress));

            mRadioMorning.setOnCheckedChangeListener(this);
            mRadioAfternoon.setOnCheckedChangeListener(this);
            mRadioAnyTime.setOnCheckedChangeListener(this);
        }

        public EditText getEditName() {
            return mEditName;
        }

        public EditText getEditEmail() {
            return mEditEmail;
        }

        public EditText getEditPhone() {
            return mEditPhone;
        }

        public EditText getEditAddress() {
            return mEditAddress;
        }

        public TextView getTextShippingTime() {
            return mTextShippingTime;
        }

        public RadioButton getRadioMorning() {
            return mRadioMorning;
        }

        public RadioButton getRadioAfternoon() {
            return mRadioAfternoon;
        }

        public RadioButton getRadioAnyTime() {
            return mRadioAnyTime;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mTextShippingTime.getCurrentTextColor()
                    != Stylish.getAppContext().getColor(R.color.gray_646464)) {
                mTextShippingTime.setTextColor(Stylish.getAppContext().getColor(R.color.gray_646464));
                mRadioMorning.setTextColor((Stylish.getAppContext().getColor(R.color.black_3f3a3a)));
                mRadioAfternoon.setTextColor((Stylish.getAppContext().getColor(R.color.black_3f3a3a)));
                mRadioAnyTime.setTextColor((Stylish.getAppContext().getColor(R.color.black_3f3a3a)));
            }
            mPresenter.onPaymentShippingTimeChanged(buttonView.getTag().toString());
        }

        private class RecipientTextWatcher implements TextWatcher {

            private EditText mEditText;

            public RecipientTextWatcher(EditText editText) {
                mEditText = editText;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mEditText.getCurrentHintTextColor()
                        != Stylish.getAppContext().getColor(R.color.gray_646464)) {
                    mEditText.setHintTextColor(Stylish.getAppContext().getColor(R.color.gray_646464));
                    mEditText.setBackgroundTintList(null);
                }
                switch (mEditText.getId()) {
                    case R.id.edit_payment_recipient_name:
                        mPresenter.onPaymentRecipientNameChanged(s.toString());
                        break;
                    case R.id.edit_payment_recipient_email:
                        mPresenter.onPaymentRecipientEmailChanged(s.toString());
                        break;
                    case R.id.edit_payment_recipient_phone:
                        mPresenter.onPaymentRecipientPhoneChanged(s.toString());
                        break;
                    case R.id.edit_payment_recipient_address:
                        mPresenter.onPaymentRecipientAddressChanged(s.toString());
                        break;
                    default:
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }
    }

    public class MethodViewHolder extends RecyclerView.ViewHolder {

        public TPDCard tpdCard;

        private Spinner mSpinnerMethods;
        private TPDForm mTpdForm;
        private TextView mTextTotalPrice;
        private TextView mTextFreightPrice;
        private TextView mTextTotalProducts;
        private TextView mTextTotalProductsPrice;
        private Button mButtonCheckOut;
        private ProgressBar mProgressCheckOut;

        public MethodViewHolder(@NonNull View itemView) {
            super(itemView);
            mSpinnerMethods = itemView.findViewById(R.id.spinner_payment_methods);
            mTpdForm = itemView.findViewById(R.id.form_payment_tpd);
            mTextTotalPrice = itemView.findViewById(R.id.text_payment_total_price);
            mTextFreightPrice = itemView.findViewById(R.id.text_payment_freight_price);
            mTextTotalProducts = itemView.findViewById(R.id.text_payment_total_products_title);
            mTextTotalProductsPrice = itemView.findViewById(R.id.text_payment_total_products);

            mProgressCheckOut = itemView.findViewById(R.id.progress_payment_checkout);
            mButtonCheckOut = itemView.findViewById(R.id.button_payment_checkout);
            mButtonCheckOut.setOnClickListener(v -> mPresenter.clickPaymentCheckOut());
        }

        public Spinner getSpinnerMethods() {
            return mSpinnerMethods;
        }

        public TPDForm getTpdForm() {
            return mTpdForm;
        }

        public TextView getTextTotalPrice() {
            return mTextTotalPrice;
        }

        public TextView getTextFreightPrice() {
            return mTextFreightPrice;
        }

        public TextView getTextTotalProducts() {
            return mTextTotalProducts;
        }

        public TextView getTextTotalProductsPrice() {
            return mTextTotalProductsPrice;
        }

        public Button getButtonCheckOut() {
            return mButtonCheckOut;
        }

        public ProgressBar getProgressCheckOut() {
            return mProgressCheckOut;
        }
    }

    public void updateData(ArrayList<CartProduct> cartProducts) {

        if (cartProducts != null) {

            mCartProducts = cartProducts;
            notifyDataSetChanged();
        }
    }
}
