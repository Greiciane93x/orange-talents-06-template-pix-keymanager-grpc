package br.com.ane.registrapix

import br.com.ane.TipoConta
import br.com.ane.enums.TipoChave
import org.hibernate.annotations.GenericGenerator
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(
        name = "uk_chave_pix",
        columnNames = ["chave"]
    )]
)
data class ChavePix(
    @field:NotNull
    @Column(nullable = false)
    val clienteId: UUID,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipo: TipoChave?,

    @field:NotBlank
    @Column(unique = true, nullable = false)
    var chave: String?,

    @field:NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val tipoDeConta: TipoConta?,

//    @Embedded
    val conta: ContaAssociada?
) {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    var id: UUID? = null

    @Column(nullable = false)
    val criadaEm: LocalDateTime = LocalDateTime.now()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChavePix

        if (clienteId != other.clienteId) return false
        if (tipo != other.tipo) return false
        if (chave != other.chave) return false
        if (tipoDeConta != other.tipoDeConta) return false
        if (conta != other.conta) return false
        if (id != other.id) return false
        if (criadaEm != other.criadaEm) return false

        return true
    }

    override fun hashCode(): Int {
        var result = clienteId?.hashCode() ?: 0
        result = 31 * result + (tipo?.hashCode() ?: 0)
        result = 31 * result + (chave?.hashCode() ?: 0)
        result = 31 * result + (tipoDeConta?.hashCode() ?: 0)
        result = 31 * result + (conta?.hashCode() ?: 0)
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + criadaEm.hashCode()
        return result
    }

    override fun toString(): String {
        return "ChavePix(clienteId=$clienteId, tipo=$tipo, chave=$chave, tipoDeConta=$tipoDeConta, conta=$conta, id=$id, criadaEm=$criadaEm)"
    }
}