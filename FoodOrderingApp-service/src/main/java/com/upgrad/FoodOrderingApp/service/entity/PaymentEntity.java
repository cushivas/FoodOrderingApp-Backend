@Table(name = "payment")
@NamedQueries(
    {
        @NamedQuery(name = "allPaymentMethods", query = "select p from PaymentEntity p ")
    }
)