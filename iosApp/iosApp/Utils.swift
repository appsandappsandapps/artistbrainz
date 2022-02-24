import Foundation
import shared

class Collector<T> : Kotlinx_coroutines_coreFlowCollector {
    let callback:(T) -> Void

    init(callback: @escaping (T) -> Void) {
        self.callback = callback
    }
    
    func emit(value: Any?) async throws -> KotlinUnit {
        callback(value as! T)
        return KotlinUnit()
    }
}
